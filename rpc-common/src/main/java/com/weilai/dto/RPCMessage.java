package com.weilai.dto;

import lombok.*;

/**
 * @ClassName RPCMessage
 * @Description: TODO
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RPCMessage {

    // rpc message type (request / response)
    private int messageType;
    // serialization type
    private int codec;
    // request data
    private Object data;

}
