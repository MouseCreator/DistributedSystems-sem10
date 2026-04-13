package mouse.univ.cases.case2;

import mouse.univ.coin.Client;
import mouse.univ.coin.Utils;
import mouse.univ.events.ContractEvent;
import mouse.univ.io.MessageIO;
import mouse.univ.lock.HashTimeLockContract;

import java.time.Duration;

public class Alice {
    public static void main(String[] args) {
        int argument = Utils.getArgument(args);
        MessageIO messageIO = new MessageIO(argument);
        Client client = new Client("Alice", messageIO);
        client.register(100);
        System.out.println("Register Alice!");
        client.barrier();
        HashTimeLockContract aliceBob = client.sendContract(Utils.hashed("12345"), Duration.ofSeconds(5), "Bob", 100);
        System.out.println("Alice created contract to send money to Bob");
        boolean b = client.tryCancel(aliceBob.getUuid());
        if (b) {
            System.out.println("Alice tried to cancel - too early!");
        } else {
            System.out.println("FAIL! Alice cancelled early!");
        }
        ContractEvent bobCarl = client.awaitContract("Bob", "Carl");
        ContractEvent carlAlice = client.awaitContract("Carl", "Alice");
        System.out.println("Alice wants to cancel - awaits");
        Utils.await(5000);
        b = client.tryCancel(aliceBob.getUuid());
        if (b) {
            System.out.println("Alice cancelled contract with Bob");
        } else {
            System.out.println("Alice failed to cancel contract with Bob");
        }
        client.awaitCancel(bobCarl.getUuid());
        client.awaitCancel(carlAlice.getUuid());
        System.out.println("Alice done!");
        client.barrier();
        messageIO.close();
    }
}
