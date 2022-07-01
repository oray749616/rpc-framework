package com.weilai.socket;

import com.weilai.annotation.ServiceScan;
import com.weilai.serialize.CommonSerializer;
import com.weilai.transport.netty.server.NettyServer;

/**
 * @ClassName NettyTestServer
 * @Description: TODO
 */
@ServiceScan
public class NettyTestServer {

    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 8899, CommonSerializer.DEFAULT_SERIALIZER);
        server.start();
    }

}
