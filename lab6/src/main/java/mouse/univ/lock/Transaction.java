package mouse.univ.lock;

import lombok.Data;

@Data
public class Transaction {
    private String uuid;
    private String sender;
    private String receiver;
    private int amount;

    public String head() {
        return uuid + ";" + amount + ";" + sender + ";" + receiver;
    }
}
