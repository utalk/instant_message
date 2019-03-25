package connector;

import kademlia.ChatService.GroupSender;
import kademlia.ChatService.GroupSender_Impl;
import kademlia.ChatService.Sender;
import kademlia.ChatService.Sender_Impl;
import kademlia.Kademlia;
import kademlia.listener.UDPListener;
import kademlia.node.Key;
import kademlia.node.Node;
import model.MessageWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class InitializerImpl implements Initializer {

    public static String[] KEYS;

    public static int current_ID;

    public static GroupSender getGroupSender() {
        return new GroupSender_Impl(KEYS);
    }

    @Override
    public MessageWrapper init(int ID) {
        KeyContainer keyContainer = KeyContainer.getInstance();
        KEYS = keyContainer.getKEYS();
        current_ID = ID;
        //TODO 在init方法中，需要设置当前好友，添加所有的好友，并完成Kademlia的初始化工作等，体现在返回值

        MessageWrapper wrapper = new MessageWrapper();
        wrapper.setCurrentUser(KEYS[ID]);
        List<String> friends = new ArrayList<>();


        Sender sender = new Sender_Impl();

        Sender_Impl.kademlia = new Kademlia(
                Key.build(KEYS[ID]),
                "udp://127.0.0.1:900" + ID
        );

        for (String keys : KEYS) {
            if (!keys.equals(KEYS[ID])) {
                friends.add(keys);
            }
        }

        if (ID != 0) {
            try {
                for(int i = 0; i< ID; i++ )
                Sender_Impl.kademlia.bootstrap(Node.builder().id(Key.build(KEYS[i])).advertisedListener(
                        new UDPListener("udp://127.0.0.1:900" + i)
                ).build());

//                sender.send(new ChatMessage(wrapper.getCurrentUser(),KEYS[0],"11sdfsd",false));
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        wrapper.setFriends(friends);
        wrapper.setSender(sender);
        GroupSender group_sender = new GroupSender_Impl(KEYS);
        wrapper.setGroupSender(group_sender);
        wrapper.setUsernameGetter(keyContainer);
        //TODO 这里还要初始化groupSender并setGroupSender
        return wrapper;
    }
}
