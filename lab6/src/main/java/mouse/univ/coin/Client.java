package mouse.univ.coin;

import mouse.univ.hash.HashFunction;
import mouse.univ.hash.Signature;
import mouse.univ.transaction.AtomicSwapTransaction;
import mouse.univ.transaction.Event;
import mouse.univ.transaction.ReceiveEvent;
import mouse.univ.transaction.TransactionEvent;

import java.security.KeyPair;
import java.util.UUID;

public class Client {

    private String name;
    private CurrencyState currencyState;

    private Signature signature;
    private HashFunction hashFunction;

    private KeyPair keyPair;

    public Client() {
    }

    public void receiveEvent(Event event) {
        if (event instanceof TransactionEvent transactionEvent) {
            AtomicSwapTransaction transaction = transactionEvent.getTransaction();
            String sender = transaction.getSender();
            ClientInfo senderInfo = currencyState.getClients().get(sender);
            String senderSignature = transaction.getSenderSignature();
            boolean valid = false;
            if (signature.verify(senderInfo.getPublicKey(), transaction.head(), senderSignature)) {
                valid = true;
            }
            if (!valid) {
                return;
            }
            if (transaction.getExpire() != null) {
                receiveLater(transaction);
            } else {
                currencyState.getTransactionList().add(transaction);
            }
        }
    }

    public void receiveUsingSecret(String x, AtomicSwapTransaction transaction) {
        if (hashFunction.hash(x).equals(transaction.getHash())
            && transaction.getReceiverSignature().equals(signature.sign(transaction.head()))) {
            currencyState.publish(new ReceiveEvent(transaction.getId(), x));
        }
    }

    public void createRefundTransaction(int money) {
        AtomicSwapTransaction atomicSwapTransaction = new AtomicSwapTransaction();
        atomicSwapTransaction.setId(UUID.randomUUID().toString());
        atomicSwapTransaction.setExpire();
        atomicSwapTransaction.setAmount(money);
        atomicSwapTransaction.setSender(name);
        atomicSwapTransaction.setReceiver(name);
        atomicSwapTransaction.setSenderPublicKey(signature.publicKey());
        currencyState.publish(new TransactionEvent(atomicSwapTransaction));
    }
}
