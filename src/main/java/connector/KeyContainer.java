package connector;

import java.util.Arrays;

public class KeyContainer implements UsernameGetter {
    private static final KeyContainer keyContainer = new KeyContainer();

    private final String[] KEYS = new String[]{
            "1e8f1fb41a86a828dc14f0f72a97388ecf22d0b0",
            "4e876501a5aa9bc0890aa7b2066a51f011a05bee",
            "6901145bb2f1b655f106b72b1f5351e34d71c96c",
            "6c7950726634ef8b9f0708879067aa935313cebe",
            "2e706bd3d73524e58321ab489ce106834627a6ae",
            "18706bd3d73524e58229ab489ce106834627a6ae",
            "23406bd3d73524e5a3c9ab489ce106834627a6ae",
            "56405bd3d73524e58229ab489ce106834627a6ae",
            "12406bd3d73524e58229ab489ce106834627a6ae"
    };

    private final String[] username_s = new String[]{
            "刘瑷玮",
            "林宇超",
            "曹嘉玮",
            "顾琦琪",
            "蔡蔚霖",
            "刘倚彤",
            "李泽斌",
            "李安迪",
            "周润发"
    };

    public static KeyContainer getInstance() {
        return keyContainer;
    }

    @Override
    public String getUsername(String key) {
        return username_s[Arrays.asList(KEYS).indexOf(key)];
    }

    public String[] getKEYS() {
        return KEYS;
    }
}
