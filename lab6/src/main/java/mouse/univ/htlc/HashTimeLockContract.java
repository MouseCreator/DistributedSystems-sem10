package mouse.univ.htlc;

import lombok.Data;
import mouse.univ.crypt.ClientSignature;
import mouse.univ.crypt.HashFunction;
import mouse.univ.crypt.RsaSignature;
import mouse.univ.crypt.SHA256;

import java.io.Serializable;
import java.security.PublicKey;
import java.time.LocalDateTime;

@Data
public class HashTimeLockContract implements Serializable {
    private String uuid;
    private PublicKey receiverPublicKey;
    private PublicKey senderPublicKey;
    private String publicHash;
    private Transaction transaction;
    private LocalDateTime timeout;

    public boolean unlock(String key, String recvSig) {
        HashFunction hashFunction = new SHA256();
        ClientSignature signature = new RsaSignature();
        return (hashFunction.hash(key).equals(publicHash) && signature.checkSignature(uuid, recvSig, receiverPublicKey));
    }

    public boolean cancel(String sendSig) {
        ClientSignature signature = new RsaSignature();
        return LocalDateTime.now().isAfter(timeout) && signature.checkSignature(uuid, sendSig, senderPublicKey);
    }
}
