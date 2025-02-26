package org.example.clipflow.utils;

import org.mindrot.jbcrypt.BCrypt;

public class EncryptionUtil {

    // 加密
    public static String encrypt(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // 验证
    public static boolean check(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

