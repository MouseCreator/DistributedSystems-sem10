package mouse.univ.cases.case2;

import mouse.univ.client.Client;
import mouse.univ.io.Utils;
import mouse.univ.events.ContractEvent;
import mouse.univ.io.MessageIO;
import mouse.univ.htlc.HashTimeLockContract;

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
        HashTimeLockContract bobCarl = client.sendContract(aliceBob.getContract().getPublicHash(), Duration.ofSeconds(3), "Carl", 50);
        System.out.println("Bob sent contract to give money to Carl");
        client.awaitCancel(aliceBob.getUuid());
        boolean b = client.tryCancel(bobCarl.getUuid());
        if (b) {
            System.out.println("Bob cancelled contract with Carl");
        } else {
            System.out.println("Bob failed to cancel contract");
        }
        System.out.println("Bob done!");
        client.barrier();
        messageIO.close();
    }
}
