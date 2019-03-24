package kademlia.ChatService;

import com.google.gson.Gson;
import kademlia.Kademlia;
import model.ChatMessage;
import model.LogMessage;
import kademlia.node.Key;

import java.util.HashMap;
import java.util.Map;


public class Sender_Impl implements Sender {

    public static Kademlia kademlia;

    @Override
    public void send(ChatMessage message) {
        Map<String, String> parameters = new HashMap<String, String>();
        try {
            System.out.println(new Gson().toJson(message));
            kademlia.send(Key.build(message.getTo()), new Gson().toJson(message));
            // 输出日志
            parameters.put("value", new Gson().toJson(new LogMessage(message.getMessageID(),
                    message.getFrom(), message.getTo(), message.getContent(),
                    message.getTime(), "send")));
//            HttpUtil.sendGet("",parameters);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
