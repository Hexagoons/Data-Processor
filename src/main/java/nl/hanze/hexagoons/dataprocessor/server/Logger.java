package nl.hanze.hexagoons.dataprocessor.server;

import java.util.concurrent.LinkedBlockingQueue;

class Logger implements Runnable {
    private final LinkedBlockingQueue<Measurement> queue;

    public Logger(LinkedBlockingQueue<Measurement> queue) {
        this.queue = queue;
    }

    public void run() {
        while (true) {
            System.out.printf("Threads active: %d Queue Size Estimate: %d\n", Thread.activeCount(), queue.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
