package com.weilai.serialize;

import com.weilai.serialize.hessian.HessianSerialize;
import com.weilai.serialize.json.JsonSerialize;
import com.weilai.serialize.kryo.KryoSerializer;
import com.weilai.serialize.protostuff.ProtostuffSerialize;

/**
 * 序列化接口，所有序列化类都需要实现这个接口
 */
public interface CommonSerializer {

    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer HESSIAN_SERIALIZER = 2;
    Integer PROTOSTUFF_SERIALIZER = 3;

    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

    /**
     * 根据序号取出序列化器
     */
    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerialize();
            case 2:
                return new HessianSerialize();
            case 3:
                return new ProtostuffSerialize();
            default:
                return null;
        }
    }

    /**
     * 序列化
     *
     * @param obj 需要序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes 序列化后的字节数组
     * @param clazz 目标类<br/>
     *              例如 {@code String.class} 的类型是 {@code Class<String>}<br/>
     *              如果不知道类的类型，使用 {@code Class<?>}
     * @param <T>   类的类型
     * @return 反序列化的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

    int getCode();
}
