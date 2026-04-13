package mouse.univ.cases.case1;

import mouse.univ.coin.Client;
import mouse.univ.coin.Utils;
import mouse.univ.io.MessageIO;
import mouse.univ.lock.HashTimeLockContract;
import mouse.univ.events.ContractEvent;
import mouse.univ.events.UnlockEvent;

import java.time.Duration;

public class Bob {
    public static void main(String[] args) {
        MessageIO messageIO = new MessageIO(Utils.getArgument(args));
        Client client = new Client("Bob", messageIO);
        client.register(50);
        System.out.println("Register Bob!");
        client.barrier();
        ContractEvent aliceBob = client.awaitContract("Alice", "Bob");
        System.out.println("Bob received contract from Alice");
        client.sendContract(aliceBob.getContract().getPublicHash(), Duration.ofMinutes(5), "Carl", 50);
        System.out.println("Bob sent contract to give money to Carl");
        ContractEvent aliceCarl = client.awaitContract("Carl", "Alice");
        UnlockEvent unlockEvent = client.awaitUnlock(aliceCarl.getUuid());
        String secret = unlockEvent.getX();
        System.out.println("Bob received secret " + secret);
        boolean b = client.tryUnlock(aliceBob.getUuid(), secret);
        if (b) {
            System.out.println("Bob unlocked contract to receive money from Alice using his key " + secret);
        } else {
            System.out.println("Bob failed to unlock contract!");
        }
        System.out.println("Bob done!");
        client.barrier();
        messageIO.close();
    }
}
