package kademlia.ChatService;

import model.ChatMessage;

/**
 * @author lzb
 * @date 2019/3/23 22:23
 */
public interface GroupSender {

    /**
     * used to send group msg
     * @param msg
     * @return false if it Failed
     */
    public boolean send(ChatMessage msg);

}
