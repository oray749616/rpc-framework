package com.weilai.codec;

import com.weilai.codec.constants.RPCConstants;
import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;
import com.weilai.enumeration.MessageType;
import com.weilai.enumeration.RPCError;
import com.weilai.exception.RPCException;
import com.weilai.serialize.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName Decoder
 * @Description: 解码拦截器
 * +---------------+---------------+-----------------+-------------+
 * |  Magic Number |  Message Type | Serializer Type | Data Length |
 * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
 * +---------------+---------------+-----------------+-------------+
 * |                          Data Bytes                           |
 * |                   Length: ${Data Length}                      |
 * +---------------------------------------------------------------+
 */

@Slf4j
public class Decoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        /*
            检验魔数 Magic Number
         */
        int magic = in.readInt();
        if (magic != RPCConstants.MAGIC_NUMBER) {
            log.error("无法识别的协议包：{}", magic);
            throw new RPCException(RPCError.UNKNOWN_PROTOCOL);
        }

        /*
            检验数据包类型 Message Type
         */
        int messageCode = in.readInt();
        Class<?> messageClass;
        if (messageCode == MessageType.REQUEST_MSG.getCode()) {
            messageClass = RPCRequest.class;
        } else if (messageCode == MessageType.RESPONSE_MSG.getCode()) {
            messageClass = RPCResponse.class;
        } else {
            log.error("无法识别的数据包类型：{}", messageCode);
            throw new RPCException(RPCError.UNKNOWN_MESSAGE_TYPE);
        }

        /*
            检验序列化器 Serializer Type
         */
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            log.error("无法识别的序列化器：{}", serializerCode);
            throw new RPCException(RPCError.UNKNOWN_SERIALIZER);
        }

        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        Object obj = serializer.deserialize(bytes, messageClass);
        out.add(obj);
    }
}
