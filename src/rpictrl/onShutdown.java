package rpictrl;

import java.io.IOException;
import rpictrl.Control.CommandHandler;
import rpictrl.client.mainClientFrame;

public class onShutdown extends Thread {
    mainClientFrame client;
    CommandHandler server;
    
    public onShutdown(mainClientFrame client) {
        this.client = client;
    }
    
    public onShutdown(CommandHandler server) {
        this.server = server;
    }
    
    @Override
    public void run() {
        if (client != null) {
            try {
                client.getClient().halt();
            } catch (IOException e) {
                System.err.println("Could not close client: " + e.getMessage());
            }
            client.dispose();
        }
        if (server != null) {
            server.halt();
        }
    }
}
