package kademlia.ChatService;

import com.google.gson.Gson;
import javafx.application.Platform;
import model.ChatMessage;
import model.LogMessage;
import viewer.context.UIContext;
import viewer.gui.uicomponents.UIMessageReceiver;

import java.util.HashMap;
import java.util.Map;

public class Receiver_Impl implements Receiver {
    //这是界面方法
    private void receiveUIMessage(ChatMessage message) {
        Platform.runLater(() -> {
            //更新JavaFX的主线程的代码放在此处
            UIMessageReceiver uiMessageReceiver = UIContext.getInstance().getUiMessageReceiver();
            if (uiMessageReceiver != null) {
                uiMessageReceiver.receiveUIMessage(message);
            }
        });
    }

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

        receiveUIMessage(m);

        // 调用界面方法
    }
}
