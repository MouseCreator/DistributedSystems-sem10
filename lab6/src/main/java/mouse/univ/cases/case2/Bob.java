package mouse.univ.cases.case2;

import mouse.univ.coin.Client;
import mouse.univ.coin.Utils;
import mouse.univ.events.ContractEvent;
import mouse.univ.events.UnlockEvent;
import mouse.univ.io.MessageIO;
import mouse.univ.lock.HashTimeLockContract;

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
        HashTimeLockContract bobCarl = client.sendContract(aliceBob.getContract().getPublicHash(), Duration.ofSeconds(1), "Carl", 50);
        System.out.println("Bob sent contract to give money to Carl");
        client.awaitCancel(aliceBob.getUuid());
        client.tryCancel(bobCarl.getUuid());
        System.out.println("Bob done!");
        client.barrier();
        messageIO.close();
    }
}
