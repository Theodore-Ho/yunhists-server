package com.example.yunhists.utils;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class UserUtils {

    public static boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return Pattern.matches(regex, email);
    }

    public static boolean validateUsername(String username) {
        return username.length() > 0 && username.length() < 15;
    }

    public static boolean validatePassword(String pwd) {
        return pwd.length() >= 6;
    }

    public static boolean validateLang(String lang) {
        List<String> langList = List.of("zh", "en");
        return langList.contains(lang);
    }

    public static String generateRandomPwd() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(62);
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }

}
