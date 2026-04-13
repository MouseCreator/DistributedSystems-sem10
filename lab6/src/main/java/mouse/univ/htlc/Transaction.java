package mouse.univ.htlc;

import lombok.Data;

import java.io.Serializable;

@Data
public class Transaction implements Serializable {
    private String sender;
    private String receiver;
    private int amount;
}
