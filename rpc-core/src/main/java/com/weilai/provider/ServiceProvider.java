package com.weilai.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ServiceProvider
 * @Description: TODO
 */
public class ServiceProvider {
    private final Map<String, Object> interfaceProvider;

    public ServiceProvider() {
        this.interfaceProvider = new HashMap<>();
    }

    public void providerServiceInterface(Object service) {
        String serviceName = service.getClass().getName();
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class clazz : interfaces) {
            interfaceProvider.put(clazz.getName(), service);
        }
    }

    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }
}
