package com.weilai.proxy;

import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;
import com.weilai.transport.RPCClient;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName ClientProxy
 * @Description: 动态代理封装Request对象，把不同的Service方法封装成统一的Request对象格式
 */

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    private RPCClient client;

    // 动态代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes()).build();

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
