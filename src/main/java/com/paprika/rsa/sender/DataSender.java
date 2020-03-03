package com.paprika.rsa.sender;

import com.paprika.rsa.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author adam
 * @date 2020/3/3
 */
@Slf4j
public class DataSender {
    private static final String IP = "127.0.0.1";
    private static final int SEND_PORT = 13000;
    private static final int RECEIVE_PORT = 15000;
    private static final int SEND_TIMES = 3;

    public static void main(String[] args) throws Exception {
        // 1.创建要用来发送的本地地址对象
        SocketAddress localAddr = new InetSocketAddress(IP, SEND_PORT);
        // 2.创建发送的Socket对象
        DatagramSocket sender = new DatagramSocket(localAddr);
        int count = 0;
        while (count < SEND_TIMES) {
            // 创建要发送的数据,字节数组
            // 3.加密要发送的数据
            byte[] data = EncryptUtil.getRSACipherText("See you again. Chen.");
            // 4.发送数据的目标地址和端口
            SocketAddress destAdd = new InetSocketAddress(IP, RECEIVE_PORT);
            // 5.创建要发送的数据包,指定内容,指定目标地址
            DatagramPacket dp = new DatagramPacket(data, data.length, destAdd);
            // 6.发送
            sender.send(dp);
            log.info("已发送第{}条数据", count + 1);
            count++;
            Thread.sleep(1000);
        }
    }
}
