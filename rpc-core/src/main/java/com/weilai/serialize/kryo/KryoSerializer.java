package com.weilai.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;
import com.weilai.enumeration.SerializerCode;
import com.weilai.exception.SerializeException;
import com.weilai.serialize.CommonSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @ClassName KryoSerializer
 * @Description: kryo序列化器
 */
public class KryoSerializer implements CommonSerializer {
    // Kryo不是线程安全的，需要借助ThreadLocal来维护其线程安全
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RPCResponse.class);
        kryo.register(RPCRequest.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             Output output = new Output(os)) {

            Kryo kryo = kryoThreadLocal.get();

            // Object -> byte: 将对象序列化为byte数组
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();

            return output.toBytes();
        } catch (Exception e) {
            throw new SerializeException("Serialization failed.");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             Input input = new Input(is)) {

            Kryo kryo = kryoThreadLocal.get();

            // byte -> Object: 将byte数组反序列化为对象
            Object obj = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();

            return clazz.cast(obj);
        } catch (Exception e) {
            throw new SerializeException("Deserialization failed.");
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }
}
