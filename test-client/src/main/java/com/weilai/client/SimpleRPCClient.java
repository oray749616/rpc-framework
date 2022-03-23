package com.weilai.client;

import com.weilai.common.RPCRequest;
import com.weilai.common.RPCResponse;
import com.weilai.transport.RPCClient;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @ClassName SimpleRPCClient
 * @Description: 使用Java BIO方式通信
 */

@AllArgsConstructor
public class SimpleRPCClient implements RPCClient {
    private String host;
    private int port;

    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println(request);

            outputStream.writeObject(request);
            outputStream.flush();

            return (RPCResponse) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
