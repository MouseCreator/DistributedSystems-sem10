package mouse.univ.crypt;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class RsaSignature implements ClientSignature {

    private static final String ALGORITHM = "SHA256withRSA";

    @Override
    public String sign(PrivateKey privateKey, String value) {
        try {
            Signature signer = Signature.getInstance(ALGORITHM);
            signer.initSign(privateKey);
            signer.update(value.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = signer.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Signature creation failed", e);
        }
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
            Signature verifier = Signature.getInstance(ALGORITHM);
            verifier.initVerify(publicKey);
            verifier.update(message.getBytes(StandardCharsets.UTF_8));
            return verifier.verify(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Signature verification failed", e);
        }
    }
}
