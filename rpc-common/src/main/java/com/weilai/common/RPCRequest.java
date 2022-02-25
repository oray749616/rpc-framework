package com.weilai.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RPCRequest
 * @Description: 定义一个通用的Request对象（消息格式）
 */

@Data
@Builder
public class RPCRequest implements Serializable {
    // 服务类名
    private String interfaceName;
    // 方法名
    private String methodName;
    // 参数列表
    private Object[] params;
    // 参数类型
    private Class<?>[] paramsTypes;
}
