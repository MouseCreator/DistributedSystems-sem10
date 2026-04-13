package mouse.univ.client;

import lombok.Data;

import java.security.PublicKey;

@Data
public class ClientInfo {
    private String name;
    private PublicKey publicKey;

    public ClientInfo(String name, PublicKey publicKey) {
        this.name = name;
        this.publicKey = publicKey;
    }
}
