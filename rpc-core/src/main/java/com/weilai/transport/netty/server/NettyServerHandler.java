package com.weilai.transport.netty.server;

import com.weilai.common.RPCRequest;
import com.weilai.common.RPCResponse;
import com.weilai.provider.ServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName NettyServerHandler
 * @Description: 消息处理器
 */
@Slf4j
@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private ServiceProvider provider;

    /**
     * 读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {
        RPCResponse response = getResponse(msg);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    /**
     * 报错处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }

    RPCResponse getResponse(RPCRequest request) {
        String interfaceName = request.getInterfaceName();
        Object service = provider.getService(interfaceName);
        Method method = null;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            Object invoke = method.invoke(service, request.getParams());
            return RPCResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            log.error("方法执行错误");
            return RPCResponse.fail();
        }
    }
}
