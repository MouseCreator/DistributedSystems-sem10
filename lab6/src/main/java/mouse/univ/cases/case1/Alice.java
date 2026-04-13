package mouse.univ.cases.case1;

import mouse.univ.client.Client;
import mouse.univ.io.Utils;
import mouse.univ.io.MessageIO;
import mouse.univ.lock.HashTimeLockContract;
import mouse.univ.events.ContractEvent;

import java.time.Duration;

public class Alice {
    public static void main(String[] args) {
        int argument = Utils.getArgument(args);
        MessageIO messageIO = new MessageIO(argument);
        Client client = new Client("Alice", messageIO);
        client.register(100);
        System.out.println("Register Alice!");
        client.barrier();
        HashTimeLockContract aliceBob = client.sendContract(Utils.hashed("12345"), Duration.ofMinutes(10), "Bob", 100);
        System.out.println("Alice created contract to send money to Bob");
        ContractEvent bobCarl = client.awaitContract("Bob", "Carl");
        ContractEvent carlAlice = client.awaitContract("Carl", "Alice");
        boolean b = client.tryUnlock(carlAlice.getUuid(), "12345");
        if (b) {
            System.out.println("Alice unlocked contract to receive money from Carl");
        } else {
            System.out.println("Alice failed to unlock contract to receive money from Carl");
        }
        client.awaitUnlock(aliceBob.getUuid());
        client.awaitUnlock(bobCarl.getUuid());
        System.out.println("Alice done!");
        client.barrier();
        messageIO.close();
    }
}
