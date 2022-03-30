package com.weilai.transport;

/**
 * 抽象RPCServer，开放封闭原则
 */
public interface RPCServer {
    void start(int port);
}
