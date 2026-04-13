package mouse.univ.cases.case1;

import mouse.univ.coin.Client;
import mouse.univ.coin.Utils;
import mouse.univ.io.MessageIO;
import mouse.univ.lock.HashTimeLockContract;
import mouse.univ.events.ContractEvent;
import mouse.univ.events.UnlockEvent;

import java.time.Duration;

public class Carl {
    public static void main(String[] args) {
        MessageIO messageIO = new MessageIO(Utils.getArgument(args));
        Client client = new Client("Carl", messageIO);
        client.register(25);
        System.out.println("Register Carl!");
        client.barrier();
        ContractEvent bobCarl = client.awaitContract("Bob", "Carl");
        System.out.println("Carl received contract from Bob");
        HashTimeLockContract carlAlice = client.sendContract(bobCarl.getContract().getPublicHash(), Duration.ofMinutes(1), "Alice", 25);
        System.out.println("Carl sent contract to give money to Alice");
        UnlockEvent unlockEvent = client.awaitUnlock(carlAlice.getUuid());
        System.out.println("Carl awaited unlock event triggered by Alice");
        String secret = unlockEvent.getX();
        boolean b = client.tryUnlock(bobCarl.getUuid(), secret);
        if (b) {
            System.out.println("Carl unlocked contract to receive money from Bob using the key " + secret);
        } else {
            System.out.println("Carl failed to unlock contract");
        }
        client.awaitUnlock(carlAlice.getUuid());
        System.out.println("Carl done!");
    }
}
