package connector;

import kademlia.ChatService.Sender;
import kademlia.ChatService.Sender_Impl;
import kademlia.Kademlia;
import kademlia.listener.UDPListener;
import kademlia.node.Key;
import kademlia.node.Node;
import model.ChatMessage;
import model.MessageWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class InitializerImpl implements Initializer {

    private static final String[] KEYS = new String[] {
            "738a4793791b8a672050cf495ac15fdae8c5e171",
            "1e8f1fb41a86a828dc14f0f72a97388ecf22d0b0",
            "4e876501a5aa9bc0890aa7b2066a51f011a05bee",
            "6901145bb2f1b655f106b72b1f5351e34d71c96c",
            "6c7950726634ef8b9f0708879067aa935313cebe",
            "2e706bd3d73524e58229ab489ce106834627a6ae"
    };

    @Override
    public MessageWrapper init(int ID) {
        //TODO 在init方法中，需要设置当前好友，添加所有的好友，并完成Kademlia的初始化工作等，体现在返回值

        MessageWrapper wrapper = new MessageWrapper();
        wrapper.setCurrentUser(KEYS[ID]);
        List<String> friends = new ArrayList<>();


        Sender sender = new Sender_Impl();

        Sender_Impl.kademlia = new Kademlia(
                Key.build(KEYS[ID]),
                "udp://127.0.0.1:900"+ Integer.toString(ID)
        );

        for(String keys: KEYS){
            if(!keys.equals(KEYS[ID])){
                friends.add(keys);
            }
        }

        if(ID != 0){
            try {
                Sender_Impl.kademlia.bootstrap(Node.builder().advertisedListener(
                        new UDPListener("udp://127.0.0.1:9000")
                ).build());

//                sender.send(new ChatMessage(wrapper.getCurrentUser(),KEYS[0],"11sdfsd",false));
            }catch (TimeoutException e){
                e.printStackTrace();
            }
        }

        wrapper.setFriends(friends);
        wrapper.setSender(sender);
        //TODO 这里还要初始化groupSender并setGroupSender
        return wrapper;
    }
}
