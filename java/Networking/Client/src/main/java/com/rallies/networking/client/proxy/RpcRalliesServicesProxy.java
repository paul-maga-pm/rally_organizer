package com.rallies.networking.client.proxy;

import com.rallies.business.api.AuthenticationException;
import com.rallies.business.api.RalliesObserver;
import com.rallies.business.api.RallyApplicationServices;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;


import com.rallies.exceptions.ExceptionBaseClass;
import com.rallies.networking.protocols.dto.*;
import com.rallies.networking.protocols.rcpprotocol.RpcRequest;
import com.rallies.networking.protocols.rcpprotocol.RpcRequestType;
import com.rallies.networking.protocols.rcpprotocol.RpcResponse;
import com.rallies.networking.protocols.rcpprotocol.RpcResponseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RpcRalliesServicesProxy implements RallyApplicationServices {
    private int port;
    private String host;

    private volatile Socket clientSocket;
    private ObjectInputStream serverResponseStream;
    private ObjectOutputStream clientRequestStream;

    private List<RalliesObserver> observers;

    private Logger logger = LogManager.getLogger(RpcRalliesServicesProxy.class);

    private BlockingQueue<RpcResponse> rpcResponseBlockingQueue;

    public RpcRalliesServicesProxy(Properties proxyServicesConfigProperties) {
        this.port = Integer.parseInt(proxyServicesConfigProperties.getProperty("server.port"));
        this.host = proxyServicesConfigProperties.getProperty("server.host");
        rpcResponseBlockingQueue = new LinkedBlockingQueue<>();
        observers = new ArrayList<>();
    }


    private void createConnectionWithServer() {
        logger.info("Creating socket connection...");
        try {
            clientSocket = new Socket(host, port);
            serverResponseStream = new ObjectInputStream(clientSocket.getInputStream());
            clientRequestStream = new ObjectOutputStream(clientSocket.getOutputStream());
            clientRequestStream.flush();
            startResponseReaderThread();
        } catch (IOException e) {
            logger.error("Can't connect to server");
            throw new ExceptionBaseClass("Can't connect to server");
        }
    }
    private void closeConnectionWithServer() {
        logger.info("Closing server connection");

            try {
                clientSocket.close();
                serverResponseStream.close();
                clientRequestStream.close();
            } catch (IOException exception) {
                logger.info("Error occurred while closing server connection: " + exception);
            }
    }

    private void startResponseReaderThread() {
        Thread responseReaderThread = new RpcResponseReader();
        responseReaderThread.start();
    }

    @Override
    public void addObserver(RalliesObserver ralliesObserver) {
        observers.add(ralliesObserver);
    }

    @Override
    public void removeObserver(RalliesObserver ralliesObserver) {
        observers.remove(ralliesObserver);
    }

    @Override
    public void notifyObserversThatParticipantWasAdded(Participant addedParticipant) {
        for(var observer : observers)
            observer.updateParticipantWasAdded(addedParticipant);
    }

    @Override
    public void notifyObserversThatRallyWasAdded(Rally addedRally) {
        for(var observer : observers)
            observer.updateRallyWasAdded(addedRally);
    }

    @Override
    public void notifyObserversThatTeamWasAdded(Team addedTeam) {
        for(var observer : observers)
            observer.updateTeamWasAdded(addedTeam);
    }

    private class RpcResponseReader extends Thread {

        @Override
        public void run() {
            logger.info("Starting response reader...");
                while (true) {
                    try {
                        RpcResponse response = (RpcResponse) serverResponseStream.readObject();
                        if (isUpdateResponse(response))
                            handleUpdateResponse(response);
                        else {
                            rpcResponseBlockingQueue.put(response);
                            if(response.getResponseType() == RpcResponseType.OK_LOGOUT)
                                return;

                            if(response.getResponseType() == RpcResponseType.LOGIN_ERROR)
                                return;
                        }

                    } catch (ClassNotFoundException e) {
                        logger.error("Couldn't find class for server response: " + e);
                    } catch (InterruptedException e) {
                        logger.error("Error occurred when adding response to response queue: " + e);
                    } catch (IOException exception) {
                        logger.error("Error occurred when reading from server response stream:" + exception);
                    }
                }

        }
    }

    private void handleUpdateResponse(RpcResponse response) {
        if (response.getResponseType() == RpcResponseType.UPDATE_RALLIES) {
            Rally addedRally = DtoUtils.getModelFromDto((RallyDto) response.getData());
            notifyObserversThatRallyWasAdded(addedRally);
        }
        if (response.getResponseType() == RpcResponseType.UPDATE_TEAMS) {
            Team addedTeam = DtoUtils.getModelFromDto((TeamDto) response.getData());
            notifyObserversThatTeamWasAdded(addedTeam);
        }
        if (response.getResponseType() == RpcResponseType.UPDATE_PARTICIPANTS) {
            Participant addedParticipant = DtoUtils.getModelFromDto((ParticipantDto) response.getData());
            notifyObserversThatParticipantWasAdded(addedParticipant);
        }
    }

    private boolean isUpdateResponse(RpcResponse response) {
        return response.getResponseType() == RpcResponseType.UPDATE_RALLIES ||
                response.getResponseType() == RpcResponseType.UPDATE_TEAMS ||
                response.getResponseType() == RpcResponseType.UPDATE_PARTICIPANTS;
    }



    private RpcResponse readResponseFromServer() {
        try {
            return rpcResponseBlockingQueue.take();
        } catch (InterruptedException e) {
            logger.error("Error occurred while reading from response queue: " + e);
        }
        return null;
    }

    private void sendRequestToServer(RpcRequest request) {
        try {
            clientRequestStream.writeObject(request);
            clientRequestStream.flush();
        } catch (IOException exception) {
            logger.error("Couldn't create output stream for sending request " + request + ": " + exception);
        }
    }

    @Override
    public void login(String username, String password) {
        createConnectionWithServer();
        logger.info("Connection created. Sending login request");
        UserDto userDto = new UserDto(username, password);

        RpcRequest request = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.LOGIN)
                .setData(userDto)
                .build();

        sendRequestToServer(request);

        RpcResponse loginResponseFromServer = readResponseFromServer();

        if (loginResponseFromServer != null) {
            if (loginResponseFromServer.getResponseType() == RpcResponseType.SERVER_ERROR){
                String error = loginResponseFromServer.getData().toString();
                logger.error("Login failed: " + error);
                closeConnectionWithServer();
                throw new ExceptionBaseClass(error);
            }

            if (loginResponseFromServer.getResponseType() == RpcResponseType.LOGIN_ERROR) {
                String error = loginResponseFromServer.getData().toString();
                logger.error("Login failed: " + error);
                closeConnectionWithServer();
                throw new AuthenticationException(error);
            }

            if (loginResponseFromServer.getResponseType() == RpcResponseType.OK_LOGIN) {
                logger.info("Login successful");
            }
        }
    }

    @Override
    public void logout() {
        RpcRequest logoutRequest = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.LOGOUT)
                .build();

        sendRequestToServer(logoutRequest);
        RpcResponse logoutResponseFromServer = readResponseFromServer();
        closeConnectionWithServer();
        if(logoutResponseFromServer != null) {
            if (logoutResponseFromServer.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = logoutResponseFromServer.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }

            if (logoutResponseFromServer.getResponseType() == RpcResponseType.OK_LOGOUT) {
                logger.info("Logout successful");
            }
        }
    }



    @Override
    public Participant addParticipant(Team participantTeam, Rally rallyParticipatesTo, String participantName) {
        RpcRequest addParticipantRequest = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.ADD_PARTICIPANT)
                .setData(DtoUtils.getDtoFromModel(participantTeam, rallyParticipatesTo, participantName))
                .build();

        sendRequestToServer(addParticipantRequest);

        RpcResponse response = readResponseFromServer();

        if (response != null) {
            if(response.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = response.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }
        }

        ParticipantDto dto = (ParticipantDto) response.getData();
        return DtoUtils.getModelFromDto(dto);
    }

    @Override
    public Collection<Participant> getAllMembersOfTeam(String teamName) {
        RpcRequest getAllMembersRequest = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.GET_ALL_MEMBERS_OF_TEAM)
                .setData(teamName)
                .build();

        sendRequestToServer(getAllMembersRequest);
        RpcResponse response = readResponseFromServer();

        if (response != null) {
            if(response.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = response.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }
        }

        ParticipantDto[] participantDtos = (ParticipantDto[]) response.getData();
        Participant[] membersOfTeam = DtoUtils.getModelFromDto(participantDtos);
        return Arrays.asList(membersOfTeam);
    }

    @Override
    public Optional<Participant> getParticipantByName(String participantName) {
        RpcRequest request = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.GET_PARTICIPANT_BY_NAME)
                .setData(participantName)
                .build();

        sendRequestToServer(request);
        var response = readResponseFromServer();

        if (response != null) {
            if(response.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = response.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }
        }

        if (response.getData() == null)
            return Optional.empty();

        return Optional.of(DtoUtils.getModelFromDto((ParticipantDto) response.getData()));
    }

    @Override
    public Rally addRally(int engineCapacity) {
        RpcRequest request = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.ADD_RALLY)
                .setData(new RallyDto("0", String.valueOf(engineCapacity), "0"))
                .build();

        sendRequestToServer(request);

        var response = readResponseFromServer();

        if (response != null) {
            if(response.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = response.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }
        }

        return DtoUtils.getModelFromDto((RallyDto) response.getData());
    }

    @Override
    public Collection<? extends Rally> getAllRallies() {
        RpcRequest request = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.GET_ALL_RALLIES)
                .build();

        sendRequestToServer(request);
        var response = readResponseFromServer();

        if (response != null) {
            if(response.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = response.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }
        }

        RallyDto[] rallyDtos = (RallyDto[]) response.getData();
        Rally[] rallies = DtoUtils.getModelFromDto(rallyDtos);
        return Arrays.asList(rallies);
    }

    @Override
    public Team addTeam(String teamName) {
        RpcRequest request = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.ADD_TEAM)
                .setData(new TeamDto("0", teamName))
                .build();

        sendRequestToServer(request);

        var response = readResponseFromServer();

        if (response != null) {
            if(response.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = response.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }
        }

        return DtoUtils.getModelFromDto((TeamDto) response.getData());
    }

    @Override
    public Collection<Team> getAllTeams() {
        RpcRequest request = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.GET_ALL_TEAMS)
                .build();

        sendRequestToServer(request);
        var response = readResponseFromServer();

        if (response != null)
            if(response.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = response.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }

        TeamDto[] teamDtos = (TeamDto[]) response.getData();
        Team[] teams = DtoUtils.getModelFromDto(teamDtos);
        return Arrays.asList(teams);
    }

    @Override
    public Optional<Team> getTeamByName(String teamName) {
        RpcRequest request = new RpcRequest
                .RpcRequestBuilder()
                .setRequestType(RpcRequestType.GET_TEAM_BY_NAME)
                .setData(new TeamDto("", teamName))
                .build();

        sendRequestToServer(request);
        var response = readResponseFromServer();

        if (response != null)
            if (response.getResponseType() == RpcResponseType.SERVER_ERROR) {
                String error = response.getData().toString();
                logger.error("Logout failed: " + error);
                throw new ExceptionBaseClass(error);
            }

        if (response.getData() == null)
            return Optional.empty();
        return Optional.of(DtoUtils.getModelFromDto((TeamDto) response.getData()));
    }

}
