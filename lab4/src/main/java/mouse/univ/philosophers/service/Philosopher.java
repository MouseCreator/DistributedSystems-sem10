package mouse.univ.philosophers.service;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Philosopher extends Thread {

    private final Table table;

    private final int id;;

    private final Controller controller;

    public Philosopher(Table table, Controller controller, int id) {
        this.id = id;
        this.table = table;
        this.controller = controller;
    }

    @Override
    public void run() {
        log.info("Philosopher {} started...", id);
        while (controller.active()) {
            boolean acquired = table.acquireForks(id);
            if (acquired) {
                controller.eat(id);
                log.info("Philosopher {} is eating...", id);
                table.releaseForks(id);
            }
            log.info("Philosopher {} is thinking...", id);
            controller.think(id);
        }
    }
}
