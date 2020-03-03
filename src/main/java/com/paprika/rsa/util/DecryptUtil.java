package com.paprika.rsa.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;

/**
 * @author adam
 * @date 2020/3/3
 */
public class DecryptUtil {
    /**
     * @param cipherText 密文
     * @return 明文数据
     * 使用RSA私钥解密数据，获得明文
     */
    public static String getRSAPlainText(byte[] cipherText) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("rsa.private"));
        RSAPrivateKey privateKey = (RSAPrivateKey) ois.readObject();
        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        ois.close();
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8) ;
    }
}
