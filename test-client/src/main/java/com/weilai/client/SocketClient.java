package com.weilai.client;

import com.weilai.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * @ClassName SocketClient
 * @Description: 测试用消费者（客户端）
 */
public class SocketClient {
    public static void main(String[] args) {
        try {
            // 建立Socket连接
            Socket socket = new Socket("127.0.0.1", 8899);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // 给服务端传递id
            outputStream.writeInt(new Random().nextInt());
            outputStream.flush();

            // 服务端查询数据，返回对应的对象
            User user = (User) inputStream.readObject();
            System.out.println("服务端返回的" + user);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("客户端启动失败");
        }
    }
}
