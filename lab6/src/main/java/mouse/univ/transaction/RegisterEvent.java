package mouse.univ.transaction;

import lombok.Data;

@Data
public class RegisterEvent implements Event{
    private String name;
    private String publicKey;
}
