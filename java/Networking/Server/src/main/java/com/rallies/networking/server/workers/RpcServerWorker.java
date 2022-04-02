package com.rallies.networking.server.workers;

import com.rallies.business.api.AuthenticationException;
import com.rallies.business.api.RallyApplicationServices;
import com.rallies.networking.protocols.dto.UserDto;
import com.rallies.networking.protocols.rcpprotocol.RpcRequest;
import com.rallies.networking.protocols.rcpprotocol.RpcRequestType;
import com.rallies.networking.protocols.rcpprotocol.RpcResponse;
import com.rallies.networking.protocols.rcpprotocol.RpcResponseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RpcServerWorker extends ServerWorker<RpcRequest, RpcResponse> {
    private Map<RpcRequest, Function<RpcRequest, RpcResponse>> requestCommandMap = new HashMap<>();
    private static final RpcResponse OK_RESPONSE = new RpcResponse.RpcResponseBuilder()
            .setResponseType(RpcResponseType.OK)
            .build();

    private Logger logger = LogManager.getLogger();

    public RpcServerWorker(Socket clientSocket, RallyApplicationServices services) {
        super(clientSocket, services);

        requestCommandMap.put(new RpcRequest.RpcRequestBuilder().setRequestType(RpcRequestType.LOGIN).build(), this::handleLoginRequest);
        requestCommandMap.put(new RpcRequest.RpcRequestBuilder().setRequestType(RpcRequestType.LOGOUT).build(), this::handleLogoutRequest);
    }



    @Override
    protected RpcResponse handleRequest(RpcRequest request) {
        var function = requestCommandMap.get(request);

        if (function == null)
            throw new IllegalArgumentException("Request argument in command map had type " + request.getRequestType());

        return function.apply(request);
    }

    private RpcResponse handleLoginRequest(RpcRequest request) {
        logger.info("Login request");

        UserDto dto = (UserDto) request.getData();

        try {
            services.login(dto.getUsername(), dto.getPassword());
            return OK_RESPONSE;
        } catch (AuthenticationException exception) {
            isConnected = false;
            return new RpcResponse
                    .RpcResponseBuilder()
                    .setResponseType(RpcResponseType.ERROR)
                    .setData(exception.getMessage())
                    .build();
        }
    }

    private RpcResponse handleLogoutRequest(RpcRequest request) {
        logger.info("Logout request");

        isConnected = false;
        return OK_RESPONSE;
    }

}
