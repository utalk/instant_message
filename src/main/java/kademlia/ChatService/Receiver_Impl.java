package kademlia.ChatService;

import com.google.gson.Gson;
import model.ChatMessage;
import model.LogMessage;

import java.util.HashMap;
import java.util.Map;

public class Receiver_Impl implements Receiver {
    @Override
    public void receive(String message) {
        ChatMessage m = new Gson().fromJson(message, ChatMessage.class);
        System.out.println("receive " + m.getContent());
        // 输出日志
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("value", new Gson().toJson(new LogMessage(m.getMessageID(),
                m.getFrom(), m.getTo(), m.getContent(),
                m.getTime(), "receive")));
//        HttpUtil.sendGet("", parameters);
        if (m.isGroup()) {
            //TODO
            // 调用群聊
        }
        // 调用界面方法
    }
}
