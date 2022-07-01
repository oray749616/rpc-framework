package com.weilai.registry;

import com.weilai.dto.RPCRequest;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {

    /**
     * 根据请求查找服务实体
     *
     * @param request 请求
     * @return 服务实体
     */
    InetSocketAddress lookupService(RPCRequest request);

}
