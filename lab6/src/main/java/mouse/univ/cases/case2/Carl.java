package mouse.univ.cases.case2;

import mouse.univ.coin.Client;
import mouse.univ.coin.Utils;
import mouse.univ.events.ContractEvent;
import mouse.univ.events.UnlockEvent;
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
        client.tryCancel(carlAlice.getUuid());
        System.out.println("Carl done!");
        client.barrier();
        messageIO.close();
    }
}
