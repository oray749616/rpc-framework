package com.weilai.transport;

import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;

public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}
