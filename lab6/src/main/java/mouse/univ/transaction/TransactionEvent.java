package mouse.univ.transaction;

import lombok.Data;

@Data
public class TransactionEvent implements Event {
    private AtomicSwapTransaction transaction;

    public TransactionEvent(AtomicSwapTransaction atomicSwapTransaction) {
        this.transaction = atomicSwapTransaction;
    }
}
