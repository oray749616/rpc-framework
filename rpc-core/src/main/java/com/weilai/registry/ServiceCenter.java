package com.weilai.registry;

import java.net.InetSocketAddress;

/**
 * @ClassName ServiceCenter
 * @Description: 注册中心接口
 */
public interface ServiceCenter {

    /**
     * 将一个服务注册到注册表
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void registry(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 根据服务名称查找服务实体
     *
     * @param serviceName 服务名称
     * @return 服务实体
     */
    InetSocketAddress lookupService(String serviceName);
}
