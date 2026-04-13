package mouse.univ.events;

import lombok.Data;

@Data
public class UnlockEvent extends Event {
    private String contractUid;
    private String receiverSignature;
    private String x;

    public UnlockEvent(String uuid, String sender, String senderSignature, String contractUid, String receiverSignature, String x) {
        super(uuid, sender, senderSignature);
        this.contractUid = contractUid;
        this.receiverSignature = receiverSignature;
        this.x = x;
    }
}
