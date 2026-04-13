package mouse.univ.coin;

import mouse.univ.crypt.ClientSignature;
import mouse.univ.crypt.RsaSignature;
import mouse.univ.io.MessageIO;
import mouse.univ.lock.HashTimeLockContract;
import mouse.univ.lock.Transaction;
import mouse.univ.events.*;

import java.security.KeyPair;
import java.security.PublicKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Client {
    private final String name;
    private final CurrencyState currencyState;
    private final MessageIO messageIO;
    private final ClientSignature signature;
    private final KeyPair keyPair;

    public Client(String name, MessageIO messageIO) {
        this.name = name;
        this.messageIO = messageIO;
        currencyState = messageIO.getCurrencyState();
        signature = new RsaSignature();;
        keyPair = signature.provideKeyPair();
    }

    public void register(int money) {
        String uuid = getUuid();
        messageIO.send(new RegisterEvent(uuid, name, sign(uuid), name, keyPair.getPublic(), money));
    }

    public HashTimeLockContract sendContract(String hash, Duration duration, String receiver, int money) {
        String uuid = getUuid();
        HashTimeLockContract contract = new HashTimeLockContract();
        contract.setUuid(uuid);
        contract.setTimeout(LocalDateTime.now().plus(duration));
        Transaction transaction = new Transaction();
        transaction.setSender(name);
        transaction.setReceiver(receiver);
        transaction.setAmount(money);
        contract.setTransaction(transaction);
        contract.setPublicHash(hash);
        contract.setSenderPublicKey(currencyState.getClients().get(name).getPublicKey());
        contract.setReceiverPublicKey(currencyState.getClients().get(receiver).getPublicKey());
        ContractEvent event = new ContractEvent(uuid, name, sign(uuid), contract);
        messageIO.send(event);
        return contract;
    }

    public boolean tryUnlock(String uuid, String key) {
        HashTimeLockContract contract = currencyState.getContracts().get(uuid);
        String signature = sign(uuid);
        if (contract.unlock(key, signature)) {
            String newUuid = getUuid();
            messageIO.send(new UnlockEvent(newUuid, name, sign(newUuid), uuid, signature, key));
            return true;
        }
        return false;
    }

    private String sign(String message) {
        return this.signature.sign(keyPair.getPrivate(), message);
    }

    public ContractEvent awaitContractForUs() {
        Event event = messageIO.awaitEvent(e -> {
            if (e instanceof ContractEvent c) {
                if (c.getContract().getReceiverPublicKey().equals(keyPair.getPublic())) {
                    return true;
                }
            }
            return false;
        });
        return (ContractEvent) event;
    }

    public UnlockEvent awaitUnlock(String uuid) {
        Event event = messageIO.awaitEvent(e -> {
            if (e instanceof UnlockEvent u) {
                if (u.getContractUid().equals(uuid)) {
                    return true;
                }
            }
            return false;
        });
        return (UnlockEvent) event;
    }

    public CancelEvent awaitCancel(String uuid) {
        Event event = messageIO.awaitEvent(e -> {
            if (e instanceof CancelEvent u) {
                if (u.getContractUid().equals(uuid)) {
                    return true;
                }
            }
            return false;
        });
        return (CancelEvent) event;
    }

    private String getUuid() {
        return UUID.randomUUID().toString();
    }

    public ContractEvent awaitContract(String sender, String receiver) {
        PublicKey pc1 = currencyState.getClients().get(sender).getPublicKey();
        PublicKey pc2 = currencyState.getClients().get(receiver).getPublicKey();
        Event event = messageIO.awaitEvent(e -> {
            System.out.println("<<< " + e.getClass().getSimpleName());
            if (e instanceof ContractEvent u) {
                if (u.getContract().getSenderPublicKey().equals(pc1) && u.getContract().getReceiverPublicKey().equals(pc2)) {
                    return true;
                }
            }
            return false;
        });
        return (ContractEvent) event;
    }

    public void barrier() {
        String uuid = Utils.uuid();
        messageIO.send(new BarrierEvent(uuid, name, sign(uuid)));
        System.out.println(name + " reached barrier!");
        for (int i = 0; i < 2; i++) {
            messageIO.awaitEvent(e -> {
                if (e instanceof BarrierEvent be) {
                    System.out.println(be.getSender() + " joins " + name + " at the barrier!");
                    return true;
                }
                return false;
            });
        }
        System.out.println(name + " passed barrier!");
    }
}
