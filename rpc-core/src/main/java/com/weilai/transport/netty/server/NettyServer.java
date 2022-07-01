package com.weilai.transport.netty.server;

import com.weilai.codec.Decoder;
import com.weilai.codec.Encoder;
import com.weilai.provider.ServiceProviderImpl2;
import com.weilai.registry.ServiceCenterImpl;
import com.weilai.serialize.CommonSerializer;
import com.weilai.transport.AbstractRpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyServer
 * @Description: 实现PRCServer接口，负责监听和发送数据
 */
@Slf4j
public class NettyServer extends AbstractRpcServer {

    private final CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        registry = new ServiceCenterImpl();
        provider = new ServiceProviderImpl2("127.0.0.1", 8899);
        this.serializer = CommonSerializer.getByCode(serializer);
        scanService();
    }

    @Override
    public void start() {
        // netty服务线程组：parent负责建立连接，child负责具体请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        System.out.println("Netty服务端启动 端口号为" + port);

        try {
            // 创建服务端启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 初始化
            bootstrap.group(bossGroup, workerGroup)
                    // 设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    // 设置服务端接受连接的最大队列长度
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态，TCP会主动探测空闲连接的有效性
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // true代表禁用Nagle算法，减小传输延迟
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new Encoder(serializer))
                                    .addLast(new Decoder())
                                    .addLast(new ServerHandler(provider));
                        }
                    });
            // 同步阻塞，绑定端口，sync方法是等待异步操作执行完毕
            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
            // 死循环监听，等待连接关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务器发生错误：", e);
        } finally {
            // 关闭相关线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
