package com.weilai.handler;

import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;
import com.weilai.enumeration.ResponseCode;
import com.weilai.provider.ServiceProvider;
import com.weilai.provider.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName RequestHandler
 * @Description: 进行过程调用的处理器
 */
@Slf4j
public class RequestHandler {

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RPCRequest request) {
        Object service = serviceProvider.getServiceProvider(request.getInterfaceName());
        log.info("服务:{} 成功调用方法:{}", request.getInterfaceName(), request.getMethodName());
        return invokeTargetMethod(request, service);
    }

    private Object invokeTargetMethod(RPCRequest request, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            result = method.invoke(service, request.getParams());
            log.info("服务:{} 成功调用方法:{}", request.getInterfaceName(), request.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RPCResponse.fail(ResponseCode.METHOD_NOT_FOUND, request.getRequestId());
        }
        return result;
    }
}
