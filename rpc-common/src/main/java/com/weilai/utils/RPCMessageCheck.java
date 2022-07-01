package com.weilai.utils;

import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;
import com.weilai.enumeration.RPCError;
import com.weilai.enumeration.ResponseCode;
import com.weilai.exception.RPCException;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName RPCMessageCheck
 * @Description: 检查响应与请求
 */

@Slf4j
public class RPCMessageCheck {

    public static final String INTERFACE_NAME = "interfaceName";

    public static void check(RPCRequest request, RPCResponse response) {
        if (response == null) {
            log.error("调用服务失败, interfaceName: {}", request.getInterfaceName());
            throw new RPCException(RPCError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ": " + request.getInterfaceName());
        }

        if (!request.getRequestId().equals(response.getRequestId())) {
            throw new RPCException(RPCError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ": " + request.getInterfaceName());
        }

        if (response.getStatusCode() == null || !response.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            log.error("调用服务失败, interfaceName: {}, RPCResponse: {}", request.getInterfaceName(), response);
            throw new RPCException(RPCError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ": " + request.getInterfaceName());
        }
    }

}
