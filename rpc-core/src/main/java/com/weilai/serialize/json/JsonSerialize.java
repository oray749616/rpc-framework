package com.weilai.serialize.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weilai.dto.RPCRequest;
import com.weilai.enumeration.SerializerCode;
import com.weilai.exception.SerializeException;
import com.weilai.serialize.CommonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @ClassName JsonSerialize
 * @Description: Json序列化器
 */

@Slf4j
public class JsonSerialize implements CommonSerializer {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("序列化时发生错误：", e);
            throw new SerializeException("Serialization failed.");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            Object obj = mapper.readValue(bytes, clazz);
            if (obj instanceof RPCRequest) {
                obj = handleRequest(obj);
            }
            return clazz.cast(obj);
        } catch (IOException e) {
            log.error("反序列化时发生错误：", e);
            throw new SerializeException("Deserialization failed.");
        }
    }

    /**
     * 由于使用JSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型
     * 需要重新判断
     */
    private Object handleRequest(Object obj) throws IOException {
        RPCRequest request = (RPCRequest) obj;
        for (int i = 0; i < request.getParamsTypes().length; i++) {
            Class<?> clazz = request.getParamsTypes()[i];
            if (!clazz.isAssignableFrom(request.getParams()[i].getClass())) {
                byte[] bytes = mapper.writeValueAsBytes(request.getParams()[i]);
                request.getParams()[i] = mapper.readValue(bytes, clazz);
            }
        }
        return request;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
