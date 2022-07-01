package com.weilai.proxy;

import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;
import com.weilai.transport.RPCClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @ClassName ClientProxy
 * @Description: 动态代理封装Request对象，把不同的Service方法封装成统一的Request对象格式
 */

@Slf4j
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {

    private final RPCClient client;

    // 动态代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoked method: [{}]", method.getName());
        RPCRequest request = RPCRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes())
                .build();

//        RPCResponse<Object> response = null;
//
//        if (client instanceof NettyClient) {
//            CompletableFuture<RPCResponse<Object>> completableFuture = (CompletableFuture<RPCResponse<Object>>) client.sendRequest(request);
//            response = completableFuture.get();
//        }
//
//        RPCMessageCheck.check(request, response);
//        return response.getData();

        RPCResponse response = client.sendRequest(request);
        return response.getData();
    }

    /**
     * 获得代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
}
