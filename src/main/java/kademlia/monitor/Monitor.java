package kademlia.monitor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;


public class Monitor {
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



    private DatagramSocket socket;
    private int port;
    private final Gson gson = new GsonBuilder()
            .create();

    public Monitor(int port) throws SocketException {
        this.port = port;
        this.socket= new DatagramSocket();
    }
    ;

    public void message(String source, String target, String type) throws IOException {
        source = username_s[findIndex(source)];
        target = username_s[findIndex(target)];
        MonitorMessage monitorMessage = MonitorMessage.builder().type(MonitorType.valueOf(type)).source(source).target(target).build();
        byte[] payload = encode(monitorMessage);
        socket.send(new DatagramPacket(
                payload,
                payload.length,
                new InetSocketAddress("127.0.0.1", port)));
    }

    public void group_message(String ID, String source, String target, long time) throws IOException {
        source = username_s[findIndex(source)];
        target = username_s[findIndex(target)];
        System.out.println(source + " "+target+" "+ID+" " + time);
        GroupMessage groupMessage = GroupMessage.builder().messageID(ID).source(source).target(target).time(time).build();
        byte[] payload = encode_group(groupMessage);
        System.out.println(payload);
        socket.send(new DatagramPacket(
                payload,
                payload.length,
                new InetSocketAddress("127.0.0.1", port)));
    }


    private MonitorMessage decode(byte[] buffer) throws UnsupportedEncodingException {
        MonitorMessage message = gson.fromJson(new String(buffer, StandardCharsets.UTF_8).trim(), MonitorMessage.class);
        return message;
    }

    private byte[] encode(MonitorMessage msg) throws UnsupportedEncodingException {
        return gson.toJson(msg).getBytes("UTF-8");
    }

    private byte[] encode_group(GroupMessage msg) throws UnsupportedEncodingException {
        return gson.toJson(msg).getBytes("UTF-8");
    }

    private int findIndex(String source) {
        for(int i = 0;i<KEYS.length;i++){
            if(KEYS[i].equals(source)){
                return i;
            }
        }
        return -1;
    }
}
