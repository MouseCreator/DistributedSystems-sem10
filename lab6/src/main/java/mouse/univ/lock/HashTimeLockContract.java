package mouse.univ.lock;

import lombok.Data;
import mouse.univ.coin.Client;
import mouse.univ.hash.ClientSignature;
import mouse.univ.hash.HashFunction;
import mouse.univ.transaction.UnlockEvent;

import java.security.PublicKey;
import java.time.LocalDateTime;

@Data
public class HashTimeLockContract {
    private PublicKey receiverPublicKey;
    private PublicKey senderPublicKey;
    private HashFunction hashFunction;
    private ClientSignature signature;
    private String publicHash;
    private Transaction transaction;
    private LocalDateTime timeout;
    private Client client;

    public boolean lock() {

    }

    public boolean unlock(String key, String aliceSignature) {
        boolean success = (hashFunction.hash(key).equals(publicHash) && signature.checkSignature(transaction.head(), aliceSignature, receiverPublicKey));
        if (success) {
            client.send(new UnlockEvent(transaction, key));
        }
    }

    public boolean cancel(String bobSignature) {
        return LocalDateTime.now().isAfter(timeout) && signature.checkSignature(transaction.head(), bobSignature, senderPublicKey);
    }
}
