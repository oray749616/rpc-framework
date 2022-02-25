package com.weilai.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RPCResponse
 * @Description: 定义一个通用的Response对象（消息格式）
 */

@Data
@Builder
public class RPCResponse implements Serializable {
    // 状态信息
    private int code;
    private String message;

    // 具体数据
    private Object data;

    public static RPCResponse success(Object data) {
        return RPCResponse.builder().code(200).data(data).build();
    }
    public static RPCResponse fail() {
        return RPCResponse.builder().code(500).message("服务端发生错误").build();
    }
}
