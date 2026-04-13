package mouse.univ.events;

import lombok.Data;

@Data
public class BarrierEvent extends Event {
    public BarrierEvent(String uuid, String sender, String senderSignature) {
        super(uuid, sender, senderSignature);
    }
}
