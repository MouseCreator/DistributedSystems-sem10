package mouse.univ.coin;

import lombok.Data;
import mouse.univ.transaction.AtomicSwapTransaction;
import mouse.univ.transaction.Event;

import java.util.HashMap;
import java.util.List;

@Data
public class CurrencyState {
    private final Resources resources;
    private List<AtomicSwapTransaction> transactionList;
    private HashMap<String, ClientInfo> clients;

    public void publish(Event event) {

    }
}
