package mouse.univ.transaction;

import lombok.Data;

@Data
public class ReceiveEvent implements Event {
    private final String uuid;
    private final String x;
}
