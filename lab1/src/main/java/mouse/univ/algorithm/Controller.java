package mouse.univ.algorithm;

import mouse.univ.algorithm.hs.HSMessage;

import java.util.List;

public interface Controller<T> {
    void await();
    boolean finished();
    void notifyLeaderFound(Long uid);
    AlgorithmMetadata getMetadata();
    void startRound();
    void send(Long neighborUid, T message);
    List<T> receiveMessages(Long uid);
}
