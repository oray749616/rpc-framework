package com.weilai.transport;

import com.weilai.dto.RPCRequest;
import com.weilai.dto.RPCResponse;
import com.weilai.serialize.CommonSerializer;

public interface RPCClient {

    int DEFAULT_SERIALIZER = CommonSerializer.DEFAULT_SERIALIZER;

    RPCResponse sendRequest(RPCRequest request);
}
