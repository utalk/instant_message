package viewer.context;

import connector.UsernameGetter;
import kademlia.ChatService.GroupSender;
import kademlia.ChatService.Sender;
import lombok.Data;
import model.ChatMessage;
import viewer.gui.uicomponents.UIMessageReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class UIContext {
    private String toUser;
    private String currentUser = "undefined";
    private boolean groupTalking;
    private Sender sender;
    private GroupSender groupSender;
    private UsernameGetter usernameGetter;

    private Map<String, List<ChatMessage>> p2pTalkingMessages = new HashMap<>();
    private List<ChatMessage> groupTalkingMessages = new ArrayList<>();
    private List<String> friendList;
    private UIMessageReceiver uiMessageReceiver;

    private static UIContext uiContext = new UIContext();

    public static UIContext getInstance() {
        return uiContext;
    }
}
