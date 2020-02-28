package nl.hanze.hexagoons.dataprocessor;

import java.io.IOException;

import nl.hanze.hexagoons.dataprocessor.server.Server;

public class Runner {

    /**
     * Runner method
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            (new Server()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
