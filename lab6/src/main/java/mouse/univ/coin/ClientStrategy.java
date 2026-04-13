package mouse.univ.coin;

import mouse.univ.transaction.Event;

public interface ClientStrategy {
    void listen(Event event);

    void run();
}
