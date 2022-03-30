package com.weilai.exception;

import com.weilai.enumeration.RPCError;

/**
 * @ClassName RPCException
 * @Description: TODO
 */
public class RPCException extends RuntimeException {

    public RPCException(RPCError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPCException(RPCError error) {
        super(error.getMessage());
    }

}
