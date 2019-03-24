package model;

import connector.UsernameGetter;
import kademlia.ChatService.GroupSender;
import kademlia.ChatService.Sender;
import lombok.Data;

import java.util.List;

@Data
public class MessageWrapper {
    private Sender sender;
    private List<String> friends;
    private String currentUser;
    private GroupSender groupSender;
    private UsernameGetter usernameGetter;
}
