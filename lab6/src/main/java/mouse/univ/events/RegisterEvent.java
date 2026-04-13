package mouse.univ.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.security.PublicKey;
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterEvent extends Event{
    private String name;
    private PublicKey publicKey;
    private int money;

    public RegisterEvent(String uuid, String sender, String senderSignature, String name, PublicKey publicKey, int money) {
        super(uuid, sender, senderSignature);
        this.name = name;
        this.publicKey = publicKey;
        this.money = money;
    }
}
