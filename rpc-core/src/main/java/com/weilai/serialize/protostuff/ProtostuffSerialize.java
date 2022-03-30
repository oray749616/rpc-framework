package com.weilai.serialize.protostuff;

import com.weilai.enumeration.SerializerCode;
import com.weilai.serialize.CommonSerializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @ClassName ProtostuffSerialize
 * @Description: Protostuff序列化器
 */
public class ProtostuffSerialize implements CommonSerializer {

    // 避免每次序列化时重新应用缓冲区空间
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] serialize(Object obj) {
        Class<?> clazz = obj.getClass();
        Schema schema = RuntimeSchema.getSchema(clazz);
        byte[] data;

        try {
            data = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }

        return data;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("PROTOSTUFF").getCode();
    }

}
