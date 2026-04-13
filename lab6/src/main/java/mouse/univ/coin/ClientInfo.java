package mouse.univ.coin;

import lombok.Data;

import java.security.PublicKey;

@Data
public class ClientInfo {
    private String name;
    private PublicKey publicKey;
}
