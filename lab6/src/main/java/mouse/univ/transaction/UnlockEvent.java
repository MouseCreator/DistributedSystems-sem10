package mouse.univ.transaction;

import lombok.Data;
import mouse.univ.lock.Transaction;

@Data
public class UnlockEvent extends Event {
    private Transaction transaction;
    private String x;

    public UnlockEvent(Transaction transaction, String x) {
        this.transaction = transaction;
        this.x = x;
    }
}
