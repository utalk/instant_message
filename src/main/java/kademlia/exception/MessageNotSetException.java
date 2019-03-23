package kademlia.exception;

public class MessageNotSetException extends RuntimeException{
    public MessageNotSetException(){super("Necessary message for message wrapper is not set");}
}
