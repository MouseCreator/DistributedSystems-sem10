package mouse.univ.events;

import lombok.Data;

import java.io.Serializable;

@Data
public class Event implements Serializable {
    protected String uuid;
    protected String sender;
    protected String senderSignature;

    public Event(String uuid, String sender, String senderSignature) {
        this.uuid = uuid;
        this.sender = sender;
        this.senderSignature = senderSignature;
    }
}
