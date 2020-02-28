package nl.hanze.hexagoons.dataprocessor.server;

import java.nio.ByteBuffer;

//This class acts as a struct (like in C/C++), reason being the optimization of function calls
public class Measurement {
    public int stn;
    public String date;
    public String time;
    public Float temp;
    public Float dewp;
    public Float stp;
    public Float slp;
    public Float visib;
    public Float wdsp;
    public Float prcp;
    public Float sndp;
    public Float cldc;
    public int frshtt;
    public Integer wnddir;

    public byte[] getBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(63);
        buffer.putInt(stn);
        buffer.put(date.getBytes());
        buffer.put(time.getBytes());
        buffer.putFloat(temp);
        buffer.putFloat(dewp);
        buffer.putFloat(stp);
        buffer.putFloat(slp);
        buffer.putFloat(visib);
        buffer.putFloat(wdsp);
        buffer.putFloat(prcp);
        buffer.putFloat(sndp);
        buffer.putFloat(cldc);
        buffer.put((byte)frshtt);
        buffer.putInt(wnddir);
        return buffer.array();
    }
}
