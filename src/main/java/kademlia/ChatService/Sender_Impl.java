package kademlia.ChatService;

import com.google.gson.Gson;
import connector.InitializerImpl;
import kademlia.Kademlia;
import kademlia.monitor.Monitor;
import model.ChatMessage;
import kademlia.node.Key;


public class Sender_Impl implements Sender {

    public static Kademlia kademlia;

    @Override
    public void send(ChatMessage message) {
        try {
            System.out.println("sender 发送 " + new Gson().toJson(message));
            kademlia.send(Key.build(message.getTo()), new Gson().toJson(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
