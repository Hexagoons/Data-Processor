package nl.hanze.hexagoons.dataprocessor.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.io.*;

abstract class Consumer implements Runnable {
    protected final LinkedBlockingQueue<Measurement> queue;
  //  protected final String filepath = System.getProperty("user.dir") + "/storage/";
    protected final String filepath = "/mnt/nfs_mounts/nfsshare/storage/";

    public Consumer(LinkedBlockingQueue<Measurement> queue) {
        this.queue = queue;
    }

    public void close() throws IOException{}
}