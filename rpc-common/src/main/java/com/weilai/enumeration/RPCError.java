package com.weilai.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC调用过程中出现的错误
 */

@AllArgsConstructor
@Getter
public enum RPCError {

    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    UNKNOWN_ERROR("出现未知错误"),
    UNKNOWN_PROTOCOL("无法识别的协议包"),
    UNKNOWN_SERIALIZER("无法识别的序列化器类型"),
    UNKNOWN_MESSAGE_TYPE("无法识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器"),
    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_SCAN_PACKAGE_NOT_FOUND("启动类ServiceScan注解缺失"),
    RESPONSE_NOT_MATCH("响应与请求不匹配");

    private final String message;

}
