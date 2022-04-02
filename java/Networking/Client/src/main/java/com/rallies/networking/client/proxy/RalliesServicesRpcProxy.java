package com.rallies.networking.client.proxy;

import com.rallies.business.api.RallyApplicationServices;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;
import com.rallies.exceptions.ExceptionBaseClass;
import com.rallies.networking.protocols.dto.UserDto;
import com.rallies.networking.protocols.rcpprotocol.RpcRequest;
import com.rallies.networking.protocols.rcpprotocol.RpcRequestType;
import com.rallies.networking.protocols.rcpprotocol.RpcResponse;
import com.rallies.networking.protocols.rcpprotocol.RpcResponseType;

import java.util.*;
import java.util.function.Consumer;

public class RalliesServicesRpcProxy extends RalliesServicesProxy<RpcRequest, RpcResponse> {
    private Map<RpcResponse, Consumer<RpcResponse>> rpcResponseConsumerMap = new HashMap<>();

    public RalliesServicesRpcProxy(Properties connectionConfigProperties) {
        super(connectionConfigProperties);

        rpcResponseConsumerMap.put(new RpcResponse.RpcResponseBuilder().setResponseType(RpcResponseType.ERROR).build(), this::handleError);
        rpcResponseConsumerMap.put(new RpcResponse.RpcResponseBuilder().setResponseType(RpcResponseType.OK).build(), (r)->{});
        rpcResponseConsumerMap.put(new RpcResponse.RpcResponseBuilder().setResponseType(RpcResponseType.UPDATE).build(), (r)->{});
    }

    private void handleError(RpcResponse rpcResponse) {
        String errorMessage = rpcResponse.getData().toString();
        throw new ExceptionBaseClass(errorMessage);
    }

    @Override
    protected void handleUpdate(RpcResponse response) {

    }

    @Override
    protected boolean isResponseAnUpdate(RpcResponse response) {
        return response.getResponseType() == RpcResponseType.UPDATE;
    }

    @Override
    protected void handleResponse(RpcResponse response) {
        var consumer = rpcResponseConsumerMap.get(response);
        consumer.accept(response);
    }

    @Override
    protected RpcRequest createLoginRequestForUser(UserDto userDto) {
        return new RpcRequest.RpcRequestBuilder().setRequestType(RpcRequestType.LOGIN).setData(userDto).build();
    }

    @Override
    protected RpcRequest createLogoutRequest() {
        return new RpcRequest.RpcRequestBuilder().setRequestType(RpcRequestType.LOGOUT).build();
    }
}
