package com.weilai.transport.netty.server;

import com.weilai.provider.ServiceProvider;
import com.weilai.transport.RPCServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;

/**
 * @ClassName NettyServer
 * @Description: 实现PRCServer接口，负责监听和发送数据
 */
@AllArgsConstructor
public class NettyServer implements RPCServer {
    private ServiceProvider provider;

    @Override
    public void start(int port) {
        // netty服务线程组：parent负责建立连接，child负责具体请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 初始化
            bootstrap.group(bossGroup, workerGroup)
                    // 设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new NettyServerInitializer(provider));
            // 同步阻塞，绑定端口
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            // 死循环监听，等待连接关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭相关线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
