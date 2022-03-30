package com.weilai.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.weilai.enumeration.SerializerCode;
import com.weilai.exception.SerializeException;
import com.weilai.serialize.CommonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName HessianSerialize
 * @Description: Hessian序列化器
 */

@Slf4j
public class HessianSerialize implements CommonSerializer {
    @Override
    public byte[] serialize(Object obj) {
        HessianOutput output = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            output = new HessianOutput(os);
            output.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            log.error("序列化时发生错误：", e);
            throw new SerializeException("Serialization failed.");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    log.error("关闭流时发生错误：", e);
                }
            }
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        HessianInput input = null;
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            input = new HessianInput(is);
            Object obj = input.readObject();
            return clazz.cast(obj);
        } catch (IOException e) {
            log.error("反序列化时发生错误：", e);
            throw new SerializeException("Deserialization failed.");
        } finally {
            if (input != null) input.close();
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("HESSIAN").getCode();
    }
}
