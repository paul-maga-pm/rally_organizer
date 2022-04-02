package com.rallies.networking.protocols.rcpprotocol;


import com.rallies.networking.protocols.Response;

import java.util.Objects;

public class RpcResponse extends Response {
    private RpcResponseType responseType;
    private Object data;

    public RpcResponseType getResponseType() {
        return responseType;
    }

    public Object getData() {
        return data;
    }

    private void setResponseType(RpcResponseType responseType) {
        this.responseType = responseType;
    }

    private void setData(Object data) {
        this.data = data;
    }

    public static class RpcResponseBuilder {
        private RpcResponse response = new RpcResponse();

        public RpcResponseBuilder setResponseType(RpcResponseType responseType) {
            response.setResponseType(responseType);
            return this;
        }

        public RpcResponseBuilder setData(Object data) {
            response.setData(data);
            return this;
        }

        public RpcResponse build() {
            return response;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RpcResponse)) return false;
        RpcResponse that = (RpcResponse) o;
        return responseType == that.responseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseType);
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "responseType=" + responseType +
                ", data=" + data +
                '}';
    }
}
