package com.rallies.networking.protocols.rcpprotocol;

import java.io.Serializable;
import java.util.Objects;

public class RpcRequest implements Serializable {
    private RpcRequestType requestType;
    Object data;

    public RpcRequestType getRequestType() {
        return requestType;
    }

    public Object getData() {
        return data;
    }

    private RpcRequest() {

    }

    private void setRequestType(RpcRequestType requestType) {
        this.requestType = requestType;
    }

    private void setData(Object data) {
        this.data = data;
    }

    public static class RpcRequestBuilder {
        private RpcRequest request = new RpcRequest();

        public RpcRequestBuilder setRequestType(RpcRequestType requestType) {
            request.setRequestType(requestType);
            return this;
        }

        public RpcRequestBuilder setData(Object data) {
            request.setData(data);
            return this;
        }

        public RpcRequest build() {
            return request;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RpcRequest)) return false;
        RpcRequest that = (RpcRequest) o;
        return requestType == that.requestType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestType);
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestType=" + requestType +
                ", data=" + data +
                '}';
    }
}
