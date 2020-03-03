package com.paprika.rsa.sender;

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
public class PublicKeyReceiver {
    private String ip;
    private int serverPort;
    private int clientPort;

    public PublicKeyReceiver(String ip, int serverPort, int clientPort) {
        this.ip = ip;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    public void receive(String fileName) throws IOException {
        // 确定写公钥数据文件流
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
        // 确定接收文件监听端口
        DatagramSocket receiver = new DatagramSocket(serverPort);
        try {
            // 单个包长度
            byte[] buf = new byte[200];
            // 构造DatagramPacket用来接收长度为200的分割PublicKey数据的分包
            DatagramPacket publicKeyData = new DatagramPacket(buf, buf.length);
            // 收到公钥分包文件后反馈确认信息"ok"，表示自己收到了发送的文件
            byte[] messageBuf;
            messageBuf = "ok".getBytes(StandardCharsets.UTF_8);
            DatagramPacket messagePkg = new DatagramPacket(messageBuf, messageBuf.length, new InetSocketAddress(ip, clientPort));
            while (true) {
                // 接收数据包
                receiver.receive(publicKeyData);
                // 判断文件是否接收完毕
                if ("end".equals(new String(publicKeyData.getData(), 0, publicKeyData.getLength()))) {
                    log.info("文件接收完毕");
                    return;
                }
                // 接收完数据包发送确认信息
                receiver.send(messagePkg);
                log.info("已发送确认消息");
                bos.write(publicKeyData.getData(), 0, publicKeyData.getLength());
                bos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文件输出流
            bos.close();
            // 关闭连接
            receiver.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String ip = "127.0.0.1";
        int serverPort = 8085;
        int clientPort = 8086;
        String fileName = "receive.public";
        new PublicKeyReceiver(ip, serverPort, clientPort).receive(fileName);
    }
}
