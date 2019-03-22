package kademlia.ChatService;

import model.ChatMessage;

public interface Sender {

    public void send(String nodeID, ChatMessage message);

}
