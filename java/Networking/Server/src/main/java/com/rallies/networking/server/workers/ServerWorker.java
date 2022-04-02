package com.rallies.networking.server.workers;

import com.rallies.business.api.RallyApplicationServices;
import com.rallies.networking.protocols.Request;
import com.rallies.networking.protocols.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public abstract class ServerWorker<Req extends Request, Res extends Response> {

    private Socket clientSocket;
    protected RallyApplicationServices services;

    protected volatile boolean isConnected;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ServerWorker(Socket clientSocket, RallyApplicationServices services) {
        this.clientSocket = clientSocket;
        this.services = services;
        isConnected = true;
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (isConnected) {
            try {
                Req request = (Req) inputStream.readObject();
                Res response = handleRequest(request);

                if (response != null)
                    sendResponse(response);
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(Res response) throws IOException {
        outputStream.writeObject(response);
        outputStream.flush();
    }

    protected  abstract Res handleRequest(Req request);
}
