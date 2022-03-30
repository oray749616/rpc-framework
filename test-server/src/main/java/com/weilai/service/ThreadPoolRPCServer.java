package com.weilai.service;

import com.weilai.WorkThread;
import com.weilai.provider.ServiceProvider;
import com.weilai.transport.RPCServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadPoolRPCServer
 * @Description: 线程池版实现
 */
public class ThreadPoolRPCServer implements RPCServer {
    private final ThreadPoolExecutor threadPoolExecutor;
    private final ServiceProvider serviceProvider;

    public ThreadPoolRPCServer(ServiceProvider serviceProvider) {
        threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 1000, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        this.serviceProvider = serviceProvider;
    }

    public ThreadPoolRPCServer(ServiceProvider serviceProvider, int corePoolSize,
                               int maximumPoolSize,
                               long keepAliveTime,
                               TimeUnit timeUnit,
                               BlockingQueue<Runnable> workQueue) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        System.out.println("服务端已启动");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                threadPoolExecutor.execute(new WorkThread(socket, serviceProvider));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
