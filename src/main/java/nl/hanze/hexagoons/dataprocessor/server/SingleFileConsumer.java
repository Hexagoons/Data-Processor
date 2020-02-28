package nl.hanze.hexagoons.dataprocessor.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class SingleFileConsumer extends Consumer {
    private final String fileName = "stationdata.bin";
    private BufferedOutputStream outputStream;

    public SingleFileConsumer(LinkedBlockingQueue<Measurement> queue) {
        super(queue);
        try {
            File file = new File(filepath + fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            outputStream = new BufferedOutputStream(new FileOutputStream(file, true));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        Measurement measurement;
        try {
            while (true) {
                while ((measurement = this.queue.take()) != null) {
                    outputStream.write(measurement.getBytes());
                    outputStream.flush();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}