package mouse.univ.coin;

import mouse.univ.transaction.Event;

public class AStrategy implements ClientStrategy {

    private Client client;

    private final int money;

    @Override
    public void listen(Event event) {
    }

    public void run() {
        client.createRefundTransaction(money);
    }
}
