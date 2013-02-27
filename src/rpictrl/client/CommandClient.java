package rpictrl.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JFrame;

public class CommandClient {
    String HOST;
    int PORT;
    boolean masterStop = false;
    
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    
    public CommandClient(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        
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
    
    public void send(String msg) {
        try {
            //System.out.println("Sending Command -> " + msg);
            this.writer.println(msg);
            String reply;

            if ((reply = this.reader.readLine()) != null) {
                System.out.println(reply);
                if (reply.equals("-1")) {
                    closeConnection();
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
        this.writer.println("-1");
        closeConnection();
    }
    
    public void closeConnection() {
        try {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.masterStop = true;
            this.writer.close();
            this.reader.close();
            this.socket.close();
        } catch (IOException e) {
            System.err.println("IOException when closing the socket: " + e.getMessage());
            System.exit(-1);
        }
    }
}
