package rpictrl.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import rpictrl.Control.Command;

public class CommandClient {
    String HOST, reply;
    int PORT;
    boolean masterStop = false;
    
    Socket socket;
    InputStream is;
    OutputStream os;
    BufferedReader reader;
    ObjectOutputStream oos;
    PrintWriter writer;
    Command prev;
    
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
            this.is = this.socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(is));
            this.os = this.socket.getOutputStream();
            //this.writer = new PrintWriter(os, true);
            this.oos = new ObjectOutputStream(os);
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
    
    public void send(Command cmd) {
        try {
            //System.out.println("Sending Command -> " + cmd);
            if (!cmd.equals(prev)) {
                this.oos.writeObject(cmd);
            }

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
    
    public void halt() throws IOException {
        this.oos.writeObject("-1");
        closeConnection();
    }
    
    public void closeConnection() throws IOException {
        this.masterStop = true;
//        this.oos.close();
//        this.reader.close();
//        this.is.close();
//        this.os.close();
//        this.socket.shutdownInput();
//        this.socket.shutdownOutput();
        System.out.println("Stopping...");
        if (!socket.isClosed()) this.socket.close();
        System.exit(0);
    }
}
