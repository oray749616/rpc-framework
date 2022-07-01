package com.weilai.provider;

import com.weilai.registry.ServiceCenter;
import com.weilai.registry.ServiceCenterImpl;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ServiceProviderImpl2
 * @Description: TODO
 */
public class ServiceProviderImpl2 {

    private final Map<String, Object> interfaceProvider;

    private final ServiceCenter register;
    private final String host;
    private final int port;

    public ServiceProviderImpl2(String host, int port) {
        this.host = host;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.register = new ServiceCenterImpl();
    }

    public void provideServiceInterface(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class clazz : interfaces) {
            interfaceProvider.put(clazz.getName(), service);
            register.registry(clazz.getName(), new InetSocketAddress(host, port));
        }
    }

    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }
}
