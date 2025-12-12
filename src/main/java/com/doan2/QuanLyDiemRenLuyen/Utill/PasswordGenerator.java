package com.doan2.QuanLyDiemRenLuyen.Utill;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#_$%";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String random(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = RANDOM.nextInt(CHARS.length());
            sb.append(CHARS.charAt(idx));
        }
        return sb.toString();
    }
}
