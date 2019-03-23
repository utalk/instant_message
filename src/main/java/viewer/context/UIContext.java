package viewer.context;

import javafx.util.Pair;
import model.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class UIContext {
    public static String toUser;
    public static String currentUser;
    public static boolean groupTalking;

    //userPort,message
    public static Pair[] pairs;

    public static HashMap<String, ChatMessage> p2pTalkingMessages;
    public static ArrayList<ChatMessage> groupTalkingMessages;
    public static ArrayList<String> friendList;

    public static void addFriend(String nodeId) {
        friendList.add(nodeId);
    }

    public static void setCurrentUser(String nodeId) {
        currentUser = nodeId;
    }
}
