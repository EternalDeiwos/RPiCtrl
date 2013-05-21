package rpictrl.socket;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import rpictrl.Control.Command;
import rpictrl.Control.CommandHandler;

public class CommandServer extends Thread {
    private static int threadCount = 0;
    private int threadNum;
    ServerSocket server;
    String HOST;
    int PORT;
    boolean masterStop;
    
    private CommandHandler handler;
    
    //Queue<String> commandQueue;
    List<CommandServerThread> threads = new LinkedList<>();
    
    public CommandServer(String host, int port, CommandHandler handler) {
        super("CommandServer-" + CommandServer.threadCount);
        this.threadNum = CommandServer.threadCount++;
        this.HOST = host;
        this.PORT = port;
        this.handler = handler;
    }
    
    @Override
    public void run() {
        try {
            this.server = new ServerSocket(this.PORT);
        } catch (BindException e) {
            System.err.println("BindException when creating ServerSocket: " + e.getMessage());
            System.exit(-1);
        } catch (SocketException e) {
            System.err.println("OtherSocketException when creating ServerSocket: " + e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("IOExcpetion when creating ServerSocket: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Unknown Excpetion when creating ServerSocket: " + e.getMessage());
            System.exit(-1);
        }
        
        System.out.println("Attempting to Listen on port " + this.PORT + "...");
        while (!this.masterStop) {
            try {
                CommandServerThread current = new CommandServerThread(this.server.accept(), this.threads, handler);
                if (current.client.isConnected()) {
                    System.out.println("Connection recieved, starting new handler thread.");
                    this.threads.add(current);
                    current.start();
                }
            } catch (SocketException e) {
                System.err.println("ThreadCreation SocketException: " + e.getMessage());
                System.exit(-1);
            } catch (IOException e) {
                System.err.println("ThreadCreation IOExcpetion: " + e.getMessage());
                System.exit(-1);
            } catch (RuntimeException e) {
                System.err.println("Unknown ThreadCreation Runtime Excpetion: " + e.getMessage());
                System.exit(-1);
            } catch (Exception e) {
                System.err.println("Unknown ThreadCreation Excpetion: " + e.getMessage());
                System.exit(-1);
            }
        }
    }
    
    public int getThreadNumber() {
        return this.threadNum;
    }
    
    public static int getThreadCount() {
        return CommandServer.threadCount;
    }
    
    public void halt() {
        this.masterStop = true;
    }
}
