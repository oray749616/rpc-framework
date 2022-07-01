package com.weilai.transport;

import com.weilai.serialize.CommonSerializer;

/**
 * 抽象RPCServer，开放封闭原则
 */
public interface RPCServer {

    int DEFAULT_SERIALIZER = CommonSerializer.DEFAULT_SERIALIZER;

    void start();

    <T> void publishService(T service, String serviceName);

}
