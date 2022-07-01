package com.weilai.transport.netty.client;

import com.weilai.dto.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName NettyClientHandler
 * @Description: TODO
 */

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
        try {
            log.info("客户端接收到消息：{}", msg);
            // 查找名为RPCResponse的AttributeKey并返回，若没有则创建
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            // 设置key的内容为从服务端传回的msg
            ctx.channel().attr(key).set(msg);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("过程调用是发生错误：");
        cause.printStackTrace();
        ctx.close();
    }
}
