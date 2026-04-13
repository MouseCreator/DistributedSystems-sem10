package mouse.univ.lock;

import java.time.LocalDateTime;

public class TimeLock {
    private Transaction transaction;
    private LocalDateTime opens;
    private String senderSignature;
}
