package com.weilai.transport.netty.client;

import com.weilai.dto.RPCResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName UnprocessedRequests
 * @Description: 未处理的请求
 */
public class UnprocessedRequests {

    private static final Map<String, CompletableFuture<RPCResponse>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RPCResponse> future) {
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
    }

    public void complete(RPCResponse response) {
        CompletableFuture<RPCResponse> future = UNPROCESSED_RESPONSE_FUTURES.remove(response.getRequestId());

        if (null != future) {
            future.complete(response);
        } else {
            throw new IllegalStateException();
        }
    }

}
