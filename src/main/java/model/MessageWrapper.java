package model;

import kademlia.ChatService.Receiver;
import kademlia.ChatService.Sender;
import lombok.Data;

@Data
public class MessageWrapper {
    private Receiver receiver;
    private Sender sender;
}
