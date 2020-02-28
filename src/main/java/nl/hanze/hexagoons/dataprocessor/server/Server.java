package nl.hanze.hexagoons.dataprocessor.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
    public static final int PORT = 7789;
    private final ServerSocket serverSocket;

    private final LinkedBlockingQueue<Measurement> queue;
    private final Logger loggerThread;
    private final Consumer consumer;
    private final ExecutorService threadPool;
    private final int totalThreads = 801;

    public Server() throws IOException {
        System.out.println("Initializing server...");
        
        this.queue = new LinkedBlockingQueue<Measurement>(40000);
        threadPool = Executors.newFixedThreadPool(totalThreads);

        consumer = new SingleFileConsumer(queue);
        loggerThread = new Logger(queue);

        serverSocket = new ServerSocket(PORT);
        serverSocket.setReceiveBufferSize(514);
    }

    public void start() {
        System.out.println("Server started.");

        Socket currentConnection;
        new Thread(consumer).start();
        new Thread(loggerThread).start();
        
        try {
            while (true) {
                currentConnection = serverSocket.accept();
                threadPool.execute(new Provider(currentConnection, queue));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Listening...");

    }
}