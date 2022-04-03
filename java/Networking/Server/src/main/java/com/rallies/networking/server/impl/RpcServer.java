package com.rallies.networking.server.impl;

import com.rallies.business.api.RallyApplicationServices;
import com.rallies.networking.server.workers.RpcWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.ExecutorServices;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServer {
    private static final int WORKER_THREAD_POOL_SIZE = 10;

    private int port;
    private ExecutorService workersThreadPool;
    private ServerSocket serverSocket;

    private Logger logger = LogManager.getLogger(RpcServer.class);
    private RallyApplicationServices services;

    public RpcServer(Properties serverConfigProperties, RallyApplicationServices services) {
        port = Integer.parseInt(serverConfigProperties.getProperty("server.port"));
        workersThreadPool = Executors.newFixedThreadPool(WORKER_THREAD_POOL_SIZE);
        this.services = services;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.error(e);
            System.exit(1);
        }
        logger.info("Server starting on " + serverSocket.getInetAddress().getHostAddress() + " " + port);
        while(true) {
            try {
                logger.info("Waiting for clients");
                Socket clientSocket = serverSocket.accept();
                logger.info("Client " + clientSocket.getInetAddress().toString() + " connected");
                RpcWorker worker = new RpcWorker(clientSocket, services);
                services.addObserver(worker);
                workersThreadPool.execute(worker);
            } catch (IOException e) {
                logger.error("Connection error occurred when client tried to connect: " + e.getCause() + "\n" + e.getMessage());
            }
        }
    }
}
