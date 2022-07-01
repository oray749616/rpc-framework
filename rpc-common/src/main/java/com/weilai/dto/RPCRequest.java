package com.weilai.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @ClassName RPCRequest
 * @Description: 定义一个通用的Request对象（消息格式）
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RPCRequest implements Serializable {
    // 请求号
    private String requestId;
    // 服务类名
    private String interfaceName;
    // 方法名
    private String methodName;
    // 参数列表
    private Object[] params;
    // 参数类型
    private Class<?>[] paramsTypes;
}
