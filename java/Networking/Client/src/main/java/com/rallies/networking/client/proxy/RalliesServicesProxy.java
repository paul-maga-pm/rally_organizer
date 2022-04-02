package com.rallies.networking.client.proxy;

import com.rallies.business.api.RallyApplicationServices;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;
import com.rallies.networking.protocols.Request;
import com.rallies.networking.protocols.Response;
import com.rallies.networking.protocols.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class RalliesServicesProxy<Req extends Request, Resp extends Response> implements RallyApplicationServices {
    private String host;
    private int serverPort;

    private Logger logger = LogManager.getLogger();

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Socket connectionSocket;
    private volatile boolean isConnected;
    private BlockingQueue<Resp> responsesBlockingQueue = new LinkedBlockingQueue<>();
    private Thread responseReaderThread;
    private Lock connectionLockOfWorker = new ReentrantLock();

    public RalliesServicesProxy(Properties connectionConfigProperties) {
        this.host = connectionConfigProperties.getProperty("host");
        this.serverPort = Integer.parseInt(connectionConfigProperties.getProperty("port"));
    }

    @Override
    public Participant addParticipant(Team participantTeam, Rally rallyParticipatesTo, String participantName) {
        return null;
    }

    @Override
    public Collection<Participant> getAllMembersOfTeam(String teamName) {
        return new ArrayList<>();
    }

    @Override
    public Optional<Participant> getParticipantByName(String participantName) {
        return Optional.empty();
    }

    @Override
    public Rally addRally(int engineCapacity) {
        return null;
    }

    @Override
    public Collection<? extends Rally> getAllRallies() {
        return new ArrayList<>();
    }

    @Override
    public Team addTeam(String teamName) {
        return null;
    }

    @Override
    public Collection<Team> getAllTeams() {
        return new ArrayList<>();
    }

    @Override
    public Optional<Team> getTeamByName(String teamName) {
        return Optional.empty();
    }

    @Override
    public void login(String username, String password) {
        logger.trace("Login request");
        initializeConnection();
        UserDto userDto = new UserDto(username, password);
        Req loginRequest = createLoginRequestForUser(userDto);
        sendRequest(loginRequest);
        Resp response = readResponse();

        logger.trace("Login response received");
        handleResponse(response);

    }

    protected abstract void handleResponse(Resp response);

    protected abstract Req createLoginRequestForUser(UserDto userDto);

    @Override
    public void logout() {
        logger.trace("Logout request");
        Req logoutRequest = createLogoutRequest();
        sendRequest(logoutRequest);
        Resp response = readResponse();
        closeConnection();
        handleResponse(response);
    }

    protected abstract Req createLogoutRequest();

    private void sendRequest(Req request) {
        try {
            logger.trace("Sending request to server");
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            logger.error(e);
            System.exit(1);
        }
    }

    private Resp readResponse() {
        Resp response = null;

        try {
            response = responsesBlockingQueue.take();
        } catch (InterruptedException e) {
            logger.error(e);
            System.exit(1);
        }

        return response;
    }

    private void initializeConnection() {
        try {
            connectionSocket = new Socket(host, serverPort);
            outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(connectionSocket.getInputStream());
            isConnected = true;
            startReaderThread();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            logger.error(exception);
            System.exit(1);
        }
    }

    private void closeConnection() {

        try {
            connectionLockOfWorker.lock();
            inputStream.close();
            outputStream.close();
            connectionSocket.close();
            connectionLockOfWorker.unlock();

            isConnected = false;
        } catch (IOException e) {
            logger.error(e);
            System.exit(1);
        }
    }

    private void startReaderThread() {
        responseReaderThread = new ResponseReader();
        responseReaderThread.start();
    }

    private class ResponseReader extends Thread {
        private Lock connectionLockOfReader = new ReentrantLock();

        @Override
        public void run() {
            while(isConnected) {
                try {
                    logger.trace("Waiting from server responses");

                    connectionLockOfReader.lock();
                    if (connectionSocket.isClosed())
                        return;
                    Resp response = (Resp) inputStream.readObject();
                    connectionLockOfReader.unlock();

                    logger.trace("Response received " + response);
                    if (response != null)
                        if (isResponseAnUpdate(response))
                            handleUpdate(response);
                        else
                            responsesBlockingQueue.put(response);
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    logger.error(e);
                }
            }
        }
    }

    protected abstract void handleUpdate(Resp response);
    protected abstract boolean isResponseAnUpdate(Resp response);
}
