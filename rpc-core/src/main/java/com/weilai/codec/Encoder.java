package com.weilai.codec;

import com.weilai.codec.constants.RPCConstants;
import com.weilai.dto.RPCRequest;
import com.weilai.enumeration.MessageType;
import com.weilai.serialize.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName Encoder
 * @Description: 编码拦截器
 * +---------------+---------------+-----------------+-------------+
 * |  Magic Number |  Message Type | Serializer Type | Data Length |
 * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
 * +---------------+---------------+-----------------+-------------+
 * |                          Data Bytes                           |
 * |                   Length: ${Data Length}                      |
 * +---------------------------------------------------------------+
 */
public class Encoder extends MessageToByteEncoder {

    private final CommonSerializer serializer;

    public Encoder (CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeInt(RPCConstants.MAGIC_NUMBER);
        if (msg instanceof RPCRequest) {
            out.writeInt(MessageType.REQUEST_MSG.getCode());
        } else {
            out.writeInt(MessageType.RESPONSE_MSG.getCode());
        }
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
