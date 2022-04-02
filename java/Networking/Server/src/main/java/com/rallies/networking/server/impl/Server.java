package com.rallies.networking.server.impl;

import com.rallies.business.api.RallyApplicationServices;
import com.rallies.networking.protocols.Request;
import com.rallies.networking.protocols.Response;
import com.rallies.networking.server.workers.ServerWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Server<Req extends Request, Res extends Response> {
    private ServerSocket serverSocket;
    private ExecutorService workersThreadPool;
    private int serverPort;
    protected RallyApplicationServices services;
    private Logger logger = LogManager.getLogger(Server.class);

    private static final int WORKER_THREAD_POOL_SIZE = 5;

    public Server(Properties serverConfigProperties, RallyApplicationServices services) {
        this.serverPort = Integer.parseInt(serverConfigProperties.getProperty("server.port"));
        this.services = services;
        workersThreadPool = Executors.newFixedThreadPool(WORKER_THREAD_POOL_SIZE);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(serverPort);
            String hostIp = serverSocket.getInetAddress().getHostAddress();
            logger.info("Server starting on: " + hostIp + " " + serverPort);
            while (true) {
                logger.info("Waiting for clients");
                Socket clientSocket = serverSocket.accept();
                logger.info("Client connected");
                ServerWorker<Req, Res> worker = createServerWorker(clientSocket);
                Runnable runnable = worker::run;
                workersThreadPool.execute(runnable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract ServerWorker<Req, Res> createServerWorker(Socket clientSocket);
}
