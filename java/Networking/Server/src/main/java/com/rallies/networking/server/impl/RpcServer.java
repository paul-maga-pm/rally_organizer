package com.rallies.networking.server.impl;

import com.rallies.business.api.RallyApplicationServices;
import com.rallies.networking.protocols.rcpprotocol.RpcRequest;
import com.rallies.networking.protocols.rcpprotocol.RpcResponse;
import com.rallies.networking.server.workers.RpcServerWorker;
import com.rallies.networking.server.workers.ServerWorker;

import java.net.Socket;
import java.util.Properties;

public class RpcServer extends Server<RpcRequest, RpcResponse> {
    public RpcServer(Properties serverConfigProperties, RallyApplicationServices services) {
        super(serverConfigProperties, services);
    }

    @Override
    protected ServerWorker<RpcRequest, RpcResponse> createServerWorker(Socket clientSocket) {
        return new RpcServerWorker(clientSocket, services);
    }
}
