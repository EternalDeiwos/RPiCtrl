package rpictrl.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import rpictrl.Control.Command;
import rpictrl.Control.CommandHandler;

public class CommandServerThread extends Thread {
    public static int thread_count = 0;
    private int thread_num;
    boolean masterStop = false;
    
    CommandHandler handler;
    
    //Queue<String> commandQueue;
    List<CommandServerThread> clientList;
    
    Socket client;
    ObjectInputStream ois;
    InputStream is;
    OutputStream os;
    PrintWriter writer;
    
    public CommandServerThread(Socket client, List<CommandServerThread> clientList, CommandHandler handler) {
        super("CommandServerThread-" + CommandServerThread.thread_count);
        this.thread_num = CommandServerThread.thread_count;
        this.client = client;
        this.handler = handler;
        this.clientList = clientList;
        CommandServerThread.thread_count++;
    }
    
    @Override
    public void run() {
        try {
            this.is = this.client.getInputStream();
            this.os = this.client.getOutputStream();
            this.ois = new ObjectInputStream(is);
            this.writer = new PrintWriter(this.client.getOutputStream(), true);
            
            Command cmd;
            Object obj;
            
            while (!masterStop) {
                obj = this.ois.readObject();
                if (obj.equals("-1")) {
                    halt();
                    break;
                } 
                if (obj instanceof Command) {
                    cmd = (Command) obj;
                    if (!cmd.equals(handler.poll())) {
                        System.out.println("Recieved Command on " + this.getName() + " -> \n" + cmd.toString());
                        this.handler.pass(cmd);
                        this.writer.println("Command Recieved on Server -> \n" + cmd.toString());
                    }
                }
                
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
            this.masterStop = true;
            this.ois.close();
            this.writer.close();
            this.is.close();
            this.os.close();
            this.client.close();
            this.clientList.remove(this);
            System.out.println(this.getName() + " shutting down...");
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
