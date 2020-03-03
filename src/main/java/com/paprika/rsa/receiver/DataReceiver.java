package com.paprika.rsa.receiver;

import com.paprika.rsa.util.DecryptUtil;
import lombok.extern.slf4j.Slf4j;
import java.net.*;

/**
 * @author adam
 * @date 2020/3/3
 */
@Slf4j
public class DataReceiver {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 15000;

    public static void main(String[] args) throws Exception {
        // 1.创建要用来接收的本地地址对象
        SocketAddress localAddr = new InetSocketAddress(IP, PORT);
        // 2.接收的服务器UDP端口
        DatagramSocket recvSocket = new DatagramSocket(localAddr);
        log.info("UDP服务器将进入接收数据状态，目标服务器地址:「{}」", recvSocket.getLocalSocketAddress());
        while (true) {
            // 3.指定接收缓冲区大小，128字节对应1024位，256字节对应2048位
            byte[] buffer = new byte[256];
            // 4.创建接收数据包对象,指定接收大小
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            // 5.阻塞等待数据到来,如果收到数据,存入packet中的缓冲区中
            recvSocket.receive(packet);
            // 解密数据并打印
            String msg = DecryptUtil.getRSAPlainText(packet.getData());
            log.info("收到数据:{}", msg);
        }
    }
}
