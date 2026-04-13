package mouse.univ.events;

import lombok.Data;

@Data
public class CancelEvent extends Event {
    private String contractUid;

    public CancelEvent(String uuid, String sender, String senderSignature, String contractUid) {
        super(uuid, sender, senderSignature);
        this.contractUid = contractUid;
    }
}
