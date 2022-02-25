package com.weilai.client;

import com.weilai.common.RPCRequest;
import com.weilai.common.RPCResponse;
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
    // 传入参数Service接口的class对象，反射封装成一个request
    private String host;
    private int port;

    // 动态代理

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes()).build();

        RPCResponse response = IOClient.sendRequest(host, port, request);

        return response.getData();
    }

    <T>T getProxy(Class clazz) {
        Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)obj;
    }
}
