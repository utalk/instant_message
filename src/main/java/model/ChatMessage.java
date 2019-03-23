package model;

public class ChatMessage {

    private String messageID;

    private String from;

    private String to;

    private String content;

    private long time;

    private boolean group;

    public ChatMessage(String messageID, String from, String to, String content, long time, boolean group) {
        this.messageID = messageID;
        this.from = from;
        this.to = to;
        this.content = content;
        this.time = time;
        this.group = group;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

