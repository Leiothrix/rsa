package com.paprika.rsa.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author adam
 * @date 2020/3/3
 */
@Slf4j
public class RSADemo {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // 1.取得RSA算法的密钥生成器对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
        // 2.初始化密钥长度为2048位
        keyPairGen.initialize(2048);
        // 3.生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 4.分别取得私钥privateKey和公钥publicKey对象
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

//        // 公钥写文件
        ObjectOutputStream oosPublic = new ObjectOutputStream(new FileOutputStream("rsa.public"));
        oosPublic.writeObject(publicKey);
        oosPublic.flush();
        oosPublic.close();
        log.info("公钥写文件成功！");

//        // 私钥写文件
        ObjectOutputStream oosPrivate = new ObjectOutputStream(new FileOutputStream("rsa.private"));
        oosPrivate.writeObject(privateKey);
        oosPrivate.flush();
        oosPrivate.close();
        log.info("私钥写文件成功！");
    }
}
