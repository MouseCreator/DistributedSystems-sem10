package mouse.univ.client;

import lombok.Data;
import mouse.univ.crypt.ClientSignature;
import mouse.univ.crypt.RsaSignature;
import mouse.univ.htlc.HashTimeLockContract;
import mouse.univ.events.ContractEvent;
import mouse.univ.events.Event;
import mouse.univ.events.RegisterEvent;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class CurrencyState {
    private ConcurrentHashMap<String, HashTimeLockContract> contracts;
    private ConcurrentHashMap<String, ClientInfo> clients;

    public CurrencyState() {
        contracts = new ConcurrentHashMap<>();
        clients = new ConcurrentHashMap<>();
    }

    public void process(Event message) {
        if (message instanceof RegisterEvent re) {
            clients.put(re.getName(), new ClientInfo(re.getName(), re.getPublicKey()));
            return;
        }
        String senderSignature = message.getSenderSignature();
        ClientSignature signature = new RsaSignature();
        boolean b = signature.checkSignature(message.getUuid(), senderSignature, clients.get(message.getSender()).getPublicKey());
        if (!b) {
            throw new RuntimeException("Unverified message received:" + message);
        }
        if (message instanceof ContractEvent ce) {
            HashTimeLockContract contract = ce.getContract();
            contracts.put(contract.getUuid(), contract);
        }
    }
}
