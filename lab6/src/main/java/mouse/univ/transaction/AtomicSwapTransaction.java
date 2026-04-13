package mouse.univ.transaction;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AtomicSwapTransaction {
    private String id;
    private int amount;
    private String sender;
    private String receiver;

    private String senderPublicKey;
    private String receiverPublicKey;

    private String senderSignature;
    private String receiverSignature;
    private LocalDateTime expire;
    private String hash;

    public String head() {
        return id + ";" + amount + ";" + sender + ";" + receiver;
    }
}
