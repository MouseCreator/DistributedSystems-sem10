package mouse.univ.crypt;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface ClientSignature {
    String sign(PrivateKey privateKey, String value);
    KeyPair provideKeyPair();
    boolean checkSignature(String message, String signature, PublicKey publicKey);
}
