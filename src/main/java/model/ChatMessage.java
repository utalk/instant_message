package model;

public class ChatMessage {

    private String messageID;

    private String from;

    private String to;

    private String content;

    private String before;

    private long time;

    private boolean group;


    public ChatMessage(String from, String to, String content, boolean group) {
        this.time = System.currentTimeMillis();
        this.messageID = from + time;
        this.from = from;
        this.to = to;
        this.content = content;
        this.group = group;
    }

    public ChatMessage(ChatMessage message) {
        this.messageID = message.messageID;
        this.from = message.from;
        this.to = message.to;
        this.content = message.content;
        this.group = message.group;
        this.time = message.time;
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

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ChatMessage)) {
            return false;
        }
        return messageID.equals(((ChatMessage) obj).getMessageID());
    }
}

