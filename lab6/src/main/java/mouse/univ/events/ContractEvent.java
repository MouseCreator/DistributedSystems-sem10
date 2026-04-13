package mouse.univ.events;

import lombok.Data;
import mouse.univ.htlc.HashTimeLockContract;

@Data
public class ContractEvent extends Event {
    private HashTimeLockContract contract;

    public ContractEvent(String uuid, String sender, String senderSignature, HashTimeLockContract contract) {
        super(uuid, sender, senderSignature);
        this.contract = contract;
    }
}
