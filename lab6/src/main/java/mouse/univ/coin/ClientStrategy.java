package mouse.univ.coin;

import mouse.univ.events.Event;

public interface ClientStrategy {
    void listen(Event event);

    void run();
}
