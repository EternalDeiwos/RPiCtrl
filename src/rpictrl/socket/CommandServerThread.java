package rpictrl.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Queue;

public class CommandServerThread extends Thread {
    public static int thread_count = 0;
    private int thread_num;
    boolean masterStop = false;
    
    Queue<String> commandQueue;
    List<CommandServerThread> clientList;
    
    Socket client;
    BufferedReader reader;
    PrintWriter writer;
    
    public CommandServerThread(Socket client, Queue<String> commandQueue, List<CommandServerThread> clientList) {
        super("CommandServerThread-" + CommandServerThread.thread_count);
        this.thread_num = CommandServerThread.thread_count;
        this.client = client;
        this.commandQueue = commandQueue;
        this.clientList = clientList;
        CommandServerThread.thread_count++;
    }
    
    @Override
    public void run() {
        try {
            this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.writer = new PrintWriter(this.client.getOutputStream(), true);
            
            String msg;
            
            while (!masterStop) {
                msg = this.reader.readLine();
                System.out.println("Recieved Command on " + this.getName() + " -> " + msg);
                this.writer.println("Recieved Command -> " + msg);
                this.commandQueue.add(msg);
            }
            
        } catch (SocketException e) {
            System.err.println("Client SocketException: " + e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Client IOExcpetion: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Unknown Client Excpetion: " + e.getMessage());
            System.exit(-1);
        }
    }
    
    public void halt() {
        try {
            this.reader.close();
            this.writer.close();
            this.client.close();
            this.clientList.remove(this);
            this.masterStop = true;
        } catch (SocketException e) {
            System.err.println("Client SocketException: " + e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Client IOExcpetion: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Unknown Client Excpetion: " + e.getMessage());
            System.exit(-1);
        }
    }
    
    public int getThreadNum() {
        return this.thread_num;
    }
}
