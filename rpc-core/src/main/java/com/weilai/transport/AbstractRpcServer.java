package com.weilai.transport;

import com.weilai.annotation.Service;
import com.weilai.annotation.ServiceScan;
import com.weilai.enumeration.RPCError;
import com.weilai.exception.RPCException;
import com.weilai.provider.ServiceProviderImpl2;
import com.weilai.registry.ServiceCenter;
import com.weilai.utils.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @ClassName AbstractRpcServer
 * @Description: TODO
 */
@Slf4j
public abstract class AbstractRpcServer implements RPCServer {

    protected String host;
    protected int port;

    protected ServiceCenter registry;
    protected ServiceProviderImpl2 provider;

    public void scanService() {
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if (!startClass.isAnnotationPresent(ServiceScan.class)) {
                log.error("启动类缺少 @ServiceScan 注解");
                throw new RPCException(RPCError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            throw new RPCException(RPCError.UNKNOWN_ERROR);
        }

        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if ("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }

        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Service.class)) {
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 " + clazz + "时发生错误");
                    continue;
                }
                if ("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface : interfaces) {
                        publishService(obj, oneInterface.getCanonicalName());
                    }
                } else {
                    publishService(obj, serviceName);
                }
            }
        }
    }

    @Override
    public <T> void publishService(T service, String serviceName) {
        provider.provideServiceInterface(service);
        registry.registry(serviceName, new InetSocketAddress(host, port));
    }
}
