package com.weilai.transport;

import com.weilai.common.RPCRequest;
import com.weilai.common.RPCResponse;

public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}
