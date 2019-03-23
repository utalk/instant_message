package kademlia.ChatService;

import model.ChatMessage;

public interface Sender {

    public void send(ChatMessage message);

}
