package mouse.univ.algorithm;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ControllerImpl<T> implements Controller<T> {

    private final Map<Long, List<T>> recordedMessages;

    private final CyclicBarrier barrier;

    private final AtomicInteger totalMessagesSent;
    private final AtomicInteger totalRounds;
    private final int totalProcesses;
    private final AtomicLong leader;
    private final AtomicInteger roundDecrement;

    public ControllerImpl(List<Long> uids) {
        recordedMessages = new ConcurrentHashMap<>();
        for (Long uid : uids) {
            recordedMessages.put(uid, new ArrayList<>());
        }
        totalProcesses = uids.size();
        this.barrier = new CyclicBarrier(totalProcesses);
        totalMessagesSent = new AtomicInteger(0);
        totalRounds = new AtomicInteger(0);
        leader = new AtomicLong(-1L);
        roundDecrement = new AtomicInteger(totalProcesses);
    }

    @Override
    public void send(Long neighborUid, T message) {
        List<T> lcrMessages = recordedMessages.get(neighborUid);
        lcrMessages.add(message);
        totalMessagesSent.incrementAndGet();
    }


    @Override
    public List<T> receiveMessages(Long uid) {
        List<T> lcrMessages = recordedMessages.get(uid);
        List<T> result = new ArrayList<>(lcrMessages);
        lcrMessages.clear();
        return result;
    }


    @Override
    public void await() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean finished() {
        return leader.get() != -1;
    }

    @Override
    public void notifyLeaderFound(Long uid) {
        leader.set(uid);
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(leader.get(), totalProcesses, totalMessagesSent.get(), totalRounds.get());
    }

    @Override
    public void startRound() {
        if (roundDecrement.get() == 1) {
            roundDecrement.set(totalProcesses);
            totalRounds.incrementAndGet();
        } else {
            roundDecrement.decrementAndGet();
        }
    }
}
