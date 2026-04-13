package mouse.univ.hash;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface ClientSignature {
    String sign(PrivateKey privateKey, String value);
    boolean verify(PublicKey publicKey, String value, String signature);
    KeyPair provideKeyPair();
    boolean checkSignature(String message, String signature, PublicKey publicKey);
}
