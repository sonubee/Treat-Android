package gllc.tech.dateapp.Objects;

/**
 * Created by bhangoo on 12/6/2016.
 */

public class Message {

    String message;
    String sender;
    String receiver;

    public Message() {}

    public Message(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


}
