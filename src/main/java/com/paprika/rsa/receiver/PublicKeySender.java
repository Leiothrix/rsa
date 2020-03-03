package com.paprika.rsa.receiver;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author adam
 * @date 2020/3/3
 */
@Slf4j
public class PublicKeySender {
    private int serverPort;
    private int clientPort;
    private String path;
    private String ip;

    public PublicKeySender(int serverPort, int clientPort, String path, String ip) {
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.path = path;
        this.ip = ip;
    }

    public void send() throws IOException {
        // 绑定接收方客户端地址
        DatagramSocket send = new DatagramSocket(clientPort);
        // 确定文件输入流
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        try {
            // 设置确认信息接收时间，3秒后未收到对方确认信息，则重新发送一次
            send.setSoTimeout(3000);
            // 确认信息包
            byte[] messageBuf = new byte[2];
            // 构造 DatagramPacket 用来接收长度为buf length的包
            DatagramPacket messagePkg = new DatagramPacket(messageBuf, messageBuf.length);
            // 文件分包长度
            byte[] buf = new byte[200];
            int len;
            // 判断条件为读到的内容是否为空
            while ((len = bis.read(buf)) != -1) {
                DatagramPacket pkg = new DatagramPacket(buf, len, new InetSocketAddress(ip, serverPort));
                send.send(pkg);
                send.receive(messagePkg);
                log.info("接收到对方确认消息:{}", new String(messagePkg.getData(), StandardCharsets.UTF_8));
            }
            // 文件传完后，发送一个结束包
            buf = "end".getBytes(StandardCharsets.UTF_8);
            DatagramPacket endPkg = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, serverPort));
            log.info("公钥发送完毕");
            send.send(endPkg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文件输入流
            bis.close();
            // 关闭连接
            send.close();
        }
    }

    public static void main(String[] args) throws IOException {
        int serverPort = 8085;
        int clientPort = 8086;
        String path = "rsa.public";
        String ip = "127.0.0.1";
        new PublicKeySender(serverPort, clientPort, path, ip).send();
    }
}
