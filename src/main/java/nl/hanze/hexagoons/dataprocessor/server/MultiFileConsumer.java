package nl.hanze.hexagoons.dataprocessor.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiFileConsumer extends Consumer {
    private final HashMap<Integer, BufferedOutputStream> openFilestreams;

    public MultiFileConsumer(LinkedBlockingQueue<Measurement> queue) {
        super(queue);
        
        openFilestreams = new HashMap<Integer, BufferedOutputStream>();

        try {
            BufferedReader csvReader = new BufferedReader(
                    new FileReader(filepath + "stations/" + "project2_2_station.csv"));
            String line;
            ArrayList<String> stnStrings = new ArrayList<>(8000);

            System.out.println("Reading stations...");
            while ((line = csvReader.readLine()) != null) {
                String[] data = line.split(",");
                for (int i = 0; i < data.length; i++) {
                    stnStrings.add(data[i]);
                }
            }
            csvReader.close();

            System.out.println("Setting up files...");
            for (int i = 0; i < stnStrings.size(); i++) {
                int stnNumber = Integer.parseInt(stnStrings.get(i));
                if (!openFilestreams.containsKey(stnNumber)) {
                    File file = new File(filepath + "stationdata/" + stnNumber + ".bin");

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileOutputStream fo = new FileOutputStream(file, true);
                    BufferedOutputStream bout = new BufferedOutputStream(fo);
                    openFilestreams.put(stnNumber, bout);
                }
            }
            System.out.println("Finished file setup.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Measurement measurement;
        try {
            while ((measurement = this.queue.poll()) != null) {
                BufferedOutputStream stream = openFilestreams.get(measurement.stn);
                stream.write(measurement.getBytes());
                stream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        for (Entry<Integer, BufferedOutputStream> keyvalue : openFilestreams.entrySet()) {
            keyvalue.getValue().close();
            openFilestreams.remove(keyvalue.getKey(), keyvalue.getValue());
            System.out.println("test");
        }
    }
}