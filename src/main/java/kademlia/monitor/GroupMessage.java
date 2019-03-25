package kademlia.monitor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class GroupMessage {
    private String messageID;
    private String source;
    private String target;
    private long time;

}
