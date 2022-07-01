package com.weilai.dto;

import com.weilai.enumeration.ResponseCode;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName RPCResponse
 * @Description: 定义一个通用的Response对象（消息格式）
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class RPCResponse implements Serializable {

    // 响应对应的请求号
    private String requestId;
    // 响应状态码
    private Integer statusCode;
    // 响应状态的补充信息
    private String message;
    // 具体数据
    private Object data;

    public static RPCResponse success(Object data, String requestId) {
//        RPCResponse response = new RPCResponse<>();
//        response.setRequestId(requestId);
//        response.setStatusCode(ResponseCode.SUCCESS.getCode());
//        response.setData(data);
        return RPCResponse.builder().requestId(requestId)
                .statusCode(ResponseCode.SUCCESS.getCode())
                .data(data)
                .build();
    }

    public static RPCResponse fail(ResponseCode code, String requestId) {
//        RPCResponse response = new RPCResponse<>();
//        response.setRequestId(requestId);
//        response.setStatusCode(code.getCode());
//        response.setMessage(code.getMessage());
        return RPCResponse.builder().requestId(requestId)
                .statusCode(code.getCode())
                .message(code.getMessage())
                .build();
    }
}
