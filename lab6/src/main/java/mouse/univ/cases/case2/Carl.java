package mouse.univ.cases.case2;

import mouse.univ.client.Client;
import mouse.univ.io.Utils;
import mouse.univ.events.ContractEvent;
import mouse.univ.io.MessageIO;
import mouse.univ.lock.HashTimeLockContract;

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
        HashTimeLockContract carlAlice = client.sendContract(bobCarl.getContract().getPublicHash(), Duration.ofSeconds(1), "Alice", 25);
        ContractEvent aliceBob = client.awaitContract("Alice", "Bob");
        client.awaitCancel(aliceBob.getUuid());
        boolean b = client.tryCancel(carlAlice.getUuid());
        if (b) {
            System.out.println("Carl cancelled contract");
        } else {
            System.out.println("Carl failed to cancel contract with Alice");
        }
        System.out.println("Carl done!");
        client.barrier();
        messageIO.close();
    }
}
