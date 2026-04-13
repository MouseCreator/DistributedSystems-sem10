package mouse.univ.events;

import lombok.Data;

@Data
public class MessageEvent extends Event {
    private String receiverName;

    public MessageEvent(String uuid, String sender, String senderSignature, String receiverName) {
        super(uuid, sender, senderSignature);
        this.receiverName = receiverName;
    }
}
