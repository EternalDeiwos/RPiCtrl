package rpictrl.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Queue;

public class CommandClient extends Thread {
    private static int threadCount = 0;
    private int threadNum = 0;
    String HOST;
    int PORT;
    boolean masterStop = false;
    
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    Queue<String> commandQueue;
    
    public CommandClient(String host, int port, Queue<String> commandQueue) {
        super("CommandClient-" + CommandClient.threadCount);
        this.threadNum = CommandClient.threadCount++;
        this.HOST = host;
        this.PORT = port;
        try {
            if ((this.commandQueue = commandQueue) == null) {
                throw new NullPointerException("F*CKING QUEUES!");
            }
        } catch (NullPointerException e) {
            System.err.println("Your commandQueue is being a silly twat: " + e.getMessage());
        }
        
        try {
            this.socket = new Socket(HOST, PORT);
            System.out.println("Connected ...");
        } catch (UnknownHostException e) {
            System.err.println("UnknownHostException with Socket: " + e.getMessage());
            System.exit(-1);
        } catch (SocketException e) {
            System.err.println("SocketException with Socket: " + e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("IOExcpetion with Socket: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Unknown Excpetion with Socket: " + e.getMessage());
            System.exit(-1);
        }
        
        try {
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (SocketException e) {
            System.err.println("SocketException with reader/writer: " + e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("IOExcpetion with reader/writer: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Unknown Excpetion with reader/writer: " + e.getMessage());
            System.exit(-1);
        }
    }
    
    @Override
    public void run() {
        try {
            while (!this.masterStop) {
                if (!this.commandQueue.isEmpty()) {
                    String msg = this.commandQueue.remove();
                    //Debug
                    System.out.println("Sending Command -> " + msg);
                    //long startTime = System.currentTimeMillis();
                    this.writer.println(msg);
                    String reply;

                    if ((reply = this.reader.readLine()) != null) {
                        //long endTime = System.currentTimeMillis();
                        //System.out.println("Process took " + (endTime-startTime) + "ms...");
                        System.out.println(reply);
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("SocketException with message: " + e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("IOExcpetion with message: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Unknown Excpetion with message: " + e.getMessage());
            System.exit(-1);
        }
    }
    
    public boolean isOpen() {
        return !this.socket.isClosed();
    }
    
    public boolean isConnected() {
        return this.socket.isConnected();
    }
    
    public void halt() {
        this.masterStop = true;
        closeConnection();
    }
    
    public int getThreadNum() {
        return this.threadNum;
    }
    
    public static int getThreadCount() {
        return CommandClient.threadCount;
    }
    
    public void closeConnection() {
        try {
            this.socket.close();
            this.writer.close();
            this.reader.close();
        } catch (IOException e) {
            System.err.println("IOException when closing the socket: " + e.getMessage());
            System.exit(-1);
        }
    }
}
