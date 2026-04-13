package mouse.univ.io;

import lombok.Getter;
import mouse.univ.client.CurrencyState;
import mouse.univ.events.Event;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

public class MessageIO {
    private final List<Event> receivedEvents = new ArrayList<>();
    private final List<Peer> peers;
    private final List<ServerSocket> serverSockets;
    private final BlockingQueue<Event> incoming;
    private final List<Thread> listenerThreads;
    @Getter
    private final CurrencyState currencyState;
    public MessageIO(int index) {
        try {
        peers = new ArrayList<>();
        currencyState = new CurrencyState();
        listenerThreads = new ArrayList<>();
        serverSockets = new ArrayList<>();
        incoming = new LinkedBlockingQueue<>();

        if (index == 0) {
            serverSockets.add(new ServerSocket(7001));
            serverSockets.add(new ServerSocket(7002));
            peers.add(new Peer("127.0.0.1", 7010));
            peers.add(new Peer("127.0.0.1", 7020));
        } else if (index == 1) {
            serverSockets.add(new ServerSocket(7010));
            serverSockets.add(new ServerSocket(7011));
            peers.add(new Peer("127.0.0.1", 7001));
            peers.add(new Peer("127.0.0.1", 7021));
        } else if (index == 2) {
            serverSockets.add(new ServerSocket(7020));
            serverSockets.add(new ServerSocket(7021));
            peers.add(new Peer("127.0.0.1", 7002));
            peers.add(new Peer("127.0.0.1", 7011));
        }
        Thread.sleep(500);

        for (Peer peer : peers) {
            peer.connect();
        }
        Thread.sleep(500);
        startListening();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Event event) {
        for (Peer peer : peers) {
            try {
                peer.out.writeObject(event);
                peer.out.flush();
            } catch (IOException e) {
                throw new RuntimeException("Failed to send event to " + peer.host + ":" + peer.port, e);
            }
        }
        currencyState.process(event);
    }

    public Event awaitEvent(Predicate<Event> predicate) {
        int index = -1;
        for (int i = 0; i < receivedEvents.size(); i++) {
            if (predicate.test(receivedEvents.get(i))) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            return receivedEvents.remove(index);
        }
        while (true) {
            try {
                Event event = incoming.take();
                if (predicate.test(event)) {
                    return event;
                } else {
                    receivedEvents.add(event);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed while waiting for event", e);
            }
        }
    }

    public void startListening() {
        for (ServerSocket serverSocket : serverSockets) {
            Thread listenerThread = new Thread(() -> listenOnSocket(serverSocket));
            listenerThread.setDaemon(true);
            listenerThread.start();
            listenerThreads.add(listenerThread);
        }
    }
    private void listenOnSocket(ServerSocket serverSocket) {
        try (Socket socket = serverSocket.accept();
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
            while (!Thread.currentThread().isInterrupted()) {
                Object message = input.readObject();
                currencyState.process((Event) message);
                incoming.put((Event) message);
            }
        } catch (EOFException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Listener error on port " + serverSocket.getLocalPort() + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void close() {
        try {
            for (Peer peer : peers) {
                peer.close();
            }
            for (ServerSocket s : serverSockets) {
                s.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Peer {
        public final String host;
        public final int port;

        private Socket socket;
        private ObjectOutputStream out;

        public Peer(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public void connect() {
            try {
                this.socket = new Socket(host, port);
                this.out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException("Failed to connect to " + host + ":" + port, e);
            }
        }

        public void close() throws IOException {
            if (out != null) out.close();
            if (socket != null) socket.close();
        }
    }
}
