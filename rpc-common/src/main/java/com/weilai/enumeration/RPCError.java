package com.weilai.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC调用过程中出现的错误
 */

@AllArgsConstructor
@Getter
public enum RPCError {

    UNKNOWN_ERROR("出现未知错误"),
    UNKNOWN_PROTOCOL("无法识别的协议包"),
    UNKNOWN_SERIALIZER("无法识别的序列化器类型"),
    UNKNOWN_MESSAGE_TYPE("无法识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器");

    private final String message;

}
