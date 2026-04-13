package mouse.univ.hash;

import java.security.*;
import java.security.spec.*;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class RsaSignature implements ClientSignature {

    private static final String ALGORITHM = "SHA256withRSA";

    @Override
    public String sign(PrivateKey privateKey, String value) {
        return "";
    }

    @Override
    public KeyPair provideKeyPair() {
        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkSignature(String message, String signature, PublicKey publicKey) {
        try {
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            byte[] messageBytes = Base64.getDecoder().decode(message);
            Signature verifier = Signature.getInstance(ALGORITHM);
            verifier.initVerify(publicKey);
            verifier.update(messageBytes);
            return verifier.verify(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Signature verification failed", e);
        }
    }
}
