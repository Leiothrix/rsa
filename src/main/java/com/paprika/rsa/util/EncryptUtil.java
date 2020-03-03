package com.paprika.rsa.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;

/**
 * @author adam
 * @date 2020/3/3
 */
public class EncryptUtil {
    /**
     * @param plainText 明文数据
     * @return 密文字节数组
     * 使用RSA公钥对指定明文进行加密
     */
    public static byte[] getRSACipherText(String plainText) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("receive.public"));
        RSAPublicKey publicKey = (RSAPublicKey) ois.readObject();
        byte[] srcData = plainText.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        ois.close();
        return cipher.doFinal(srcData);
    }
}
