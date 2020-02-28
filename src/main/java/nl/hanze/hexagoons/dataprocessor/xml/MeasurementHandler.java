package nl.hanze.hexagoons.dataprocessor.xml;

import java.util.ArrayList;

import nl.hanze.hexagoons.dataprocessor.server.Measurement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MeasurementHandler extends DefaultHandler {
    private String currentTag;
    private final ArrayList<Measurement> measurements;
    private Measurement currMeasurement;

    public MeasurementHandler() {
        this.measurements = new ArrayList<>(10);
        currMeasurement = new Measurement();
    }

    @Override
    public void startDocument() throws SAXException {
        measurements.clear();
        currentTag = "";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentTag = qName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("MEASUREMENT")) {
            measurements.add(currMeasurement);
            currMeasurement = new Measurement();
        }

        currentTag = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (currentTag.equals("") || currentTag.equalsIgnoreCase("MEASUREMENT")
                || currentTag.equalsIgnoreCase("WEATHERDATA")) {
            return;
        }

        String content = new String(ch, start, length);
        switch (currentTag.toUpperCase()) {
        case "STN":
        currMeasurement.stn = Integer.parseInt(content);
            break;
        case "WNDDIR":
        currMeasurement.wnddir = Integer.parseInt(content);
            break;
        case "DATE":
        currMeasurement.date = content;
            break;
        case "TIME":
        currMeasurement.time = content;
            break;
        case "TEMP":
        currMeasurement.temp = Float.parseFloat(content);
            break;
        case "DEWP":
        currMeasurement.dewp = Float.parseFloat(content);
            break;
        case "STP":
        currMeasurement.stp = Float.parseFloat(content);
            break;
        case "SLP":
        currMeasurement.slp = Float.parseFloat(content);
            break;
        case "VISIB":
        currMeasurement.visib = Float.parseFloat(content);
            break;
        case "WDSP":
        currMeasurement.wdsp = Float.parseFloat(content);
            break;
        case "PRCP":
        currMeasurement.prcp = Float.parseFloat(content);
            break;
        case "SNDP":
        currMeasurement.sndp = Float.parseFloat(content);
            break;
        case "CLDC":
        currMeasurement.cldc = Float.parseFloat(content);
            break;
        case "FRSHTT":
        currMeasurement.frshtt = Integer.parseInt(content);
            break;
        }
    }

    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }
}
