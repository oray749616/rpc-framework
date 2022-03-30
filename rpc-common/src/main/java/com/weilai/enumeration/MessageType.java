package com.weilai.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {

    REQUEST_MSG(0),
    RESPONSE_MSG(1);

    private final int code;

}
