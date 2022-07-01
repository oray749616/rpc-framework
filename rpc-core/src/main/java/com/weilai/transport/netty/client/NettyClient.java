package com.weilai.transport.netty.client;

import com.weilai.codec.Decoder;
import com.weilai.codec.Encoder;
import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;
import com.weilai.serialize.kryo.KryoSerializer;
import com.weilai.transport.RPCClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyClient
 * @Description: TODO
 */
@Slf4j
@AllArgsConstructor
public class NettyClient implements RPCClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private String host;
    private int port;

    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                                .addLast(new Decoder())
                                .addLast(new Encoder(new KryoSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public RPCResponse sendRequest(RPCRequest request) {

        try {
            // 建立连接
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            log.info("客户端连接到服务器 {}:{}", host, port);
            Channel channel = channelFuture.channel();
            if (channel != null) {
                channel.writeAndFlush(request).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("客户端发送消息：{}", request.toString());
                    } else {
                        log.error("发送消息时发生错误：", future.cause());
                    }
                });
                // 阻塞主线程直到有线程监听到关闭事件
                channel.closeFuture().sync();
                // 通过AttributeKey方式阻塞获得返回结果
                AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
                return channel.attr(key).get();
            }
        } catch (InterruptedException e) {
            log.error("发送消息时发生错误：", e);
        } finally {
            // 关闭相关线程组资源
            eventLoopGroup.shutdownGracefully();
        }

        return null;
    }
}
