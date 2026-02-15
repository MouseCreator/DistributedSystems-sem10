package mouse.univ.algorithm.lcr;

import mouse.univ.algorithm.AlgorithmMetadata;

import java.util.List;

public interface LcrController {
    void send(Long neighborUid, LcrMessage message);
    List<LcrMessage> receiveMessages(Long uid);
    void await();
    boolean finished();
    void notifyLeaderFound(Long uid);
    AlgorithmMetadata getMetadata();
    void startRound();
}
