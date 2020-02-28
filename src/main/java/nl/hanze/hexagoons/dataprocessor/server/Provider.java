package nl.hanze.hexagoons.dataprocessor.server;

import nl.hanze.hexagoons.dataprocessor.xml.MeasurementHandler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

class Provider implements Runnable {
    private Socket connection;
    private final LinkedBlockingQueue<Measurement> queue;

    private SAXParser parser;
    private final MeasurementHandler saxHandler;

    private String line;
    private final StringBuilder sBuilder;
    private final InputSource stringInputSource;
    private BufferedReader bufferedReader;

    private HashMap<Integer, ArrayList<Measurement>> measurementCache;

    public Provider(Socket connection, LinkedBlockingQueue<Measurement> queue) {
        this.connection = connection;
        measurementCache = new HashMap<Integer, ArrayList<Measurement>>(10);
        this.queue = queue;
        sBuilder = new StringBuilder(4096);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        stringInputSource = new InputSource();
        try {
            this.parser = factory.newSAXParser();
            bufferedReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        saxHandler = new MeasurementHandler();
    }

    public void run() {
        try {
            //Read the lines in the buffered reader(socket)
            while ((line = bufferedReader.readLine()) != null) {
                //Appen the read line
                sBuilder.append(line.trim());
                
                //Check if we reached the end of the xml file stream
                if (line.equalsIgnoreCase("</WEATHERDATA>")) {
                    //Create a string reader and parse the xml file
                    StringReader reader = new StringReader(sBuilder.toString());
                    stringInputSource.setCharacterStream(reader);
                    parser.parse(stringInputSource, saxHandler);

                    //Get the result of the parser using the handler
                    ArrayList<Measurement> measurements = saxHandler.getMeasurements();

                    //Loop through the measurements that were send
                    for(Measurement measurement : measurements){
                        //Validate that there are no errors, or fix them if there are
                        validateMeasurement(measurement);

                        //Manage the cache
                        if(measurementCache.containsKey(measurement.stn)){
                            ArrayList<Measurement> cache = measurementCache.get(measurement.stn);
                            if(cache.size() == 30){
                                cache.remove(0);
                            }
                            cache.add(measurement);
                        }
                        else{
                            measurementCache.put(measurement.stn, new ArrayList<>());
                            measurementCache.get(measurement.stn).add(measurement);
                        }
                        //Add the measurement to the blocking queue
                        queue.put(measurement);
                    }

                    //Reset the string builder and close the reader
                    sBuilder.setLength(0);
                    reader.close();
                }
            }
        } catch (IOException | SAXException | InterruptedException e) {
            e.printStackTrace(); //Print any exceptions
        }finally {
            try {
                //Close the socket
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void validateMeasurement(Measurement measurement){
        if (!measurementCache.containsKey(measurement.stn))
            measurementCache.put(measurement.stn, new ArrayList<>(10));

        if (measurement.temp == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).temp != null)
                    temp += measurementCache.get(measurement.stn).get(x).temp;
            }
            measurement.temp = temp / measurementCache.size();
        }
        if (measurement.dewp == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).dewp != null)
                    temp += measurementCache.get(measurement.stn).get(x).dewp;
            }
            measurement.dewp = temp / measurementCache.size();
        }
        if (measurement.stp == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).stp != null)
                    temp += measurementCache.get(measurement.stn).get(x).stp;
            }
            measurement.stp = temp / measurementCache.size();
        }
        if (measurement.slp == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).slp != null)
                    temp += measurementCache.get(measurement.stn).get(x).slp;
            }
            measurement.slp = temp / measurementCache.size();
        }
        if (measurement.visib == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).visib != null)
                    temp += measurementCache.get(measurement.stn).get(x).visib;
            }
            measurement.visib = temp / measurementCache.size();
        }
        if (measurement.wdsp == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).wdsp != null)
                    temp += measurementCache.get(measurement.stn).get(x).wdsp;
            }
            measurement.wdsp = temp / measurementCache.size();
        }
        if (measurement.prcp == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).prcp != null)
                    temp += measurementCache.get(measurement.stn).get(x).prcp;
            }
            measurement.prcp = temp / measurementCache.size();
        }
        if (measurement.sndp == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).sndp != null)
                    temp += measurementCache.get(measurement.stn).get(x).sndp;
            }
            measurement.sndp = temp / measurementCache.size();
        }
        if (measurement.cldc == null) {
            float temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).cldc != null)
                    temp += measurementCache.get(measurement.stn).get(x).cldc;
            }
            measurement.cldc = temp / measurementCache.size();
        }
        if (measurement.wnddir == null) {
            int temp = 0;
            for (int x = 0; x < measurementCache.get(measurement.stn).size(); x++) {
                if (measurementCache.get(measurement.stn).get(x).wnddir != null)
                    temp += measurementCache.get(measurement.stn).get(x).wnddir;
            }
            measurement.wnddir = temp / measurementCache.size();
        }
    }
}