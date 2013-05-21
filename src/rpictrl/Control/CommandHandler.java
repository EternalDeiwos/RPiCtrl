package rpictrl.Control;

import java.net.InetAddress;
import java.net.UnknownHostException;
import rpictrl.onShutdown;
import rpictrl.socket.CommandServer;

public class CommandHandler extends Thread {
    public static int threadCount = 0;
    private int threadNum;
    boolean masterStop = false;
    
    Bridge bridge;
    
    //Queue<String> commandQueue = new LinkedList<String>();
    
    CommandServer server;
    private Command cmd = new Command(Command.ThrustCommand.OFF, Command.DirectionCommand.CENTER, Command.SpeedCommand.NONE, Command.MiscCommand.NONE);
    
    public CommandHandler() {
        super("CommandHandler-" + CommandHandler.threadCount);
        this.threadNum = CommandHandler.threadCount;
        CommandHandler.threadCount++;
    }
    
    public Command poll() {
        return cmd;
    }
    
    public void pass(Command cmd) {
        this.cmd = cmd;
    }
    
    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new onShutdown(this));
        try {
            bridge = new Bridge(true, false);
            server = new CommandServer(InetAddress.getLocalHost().getHostAddress(), 53789, this);
        } catch (UnknownHostException e) {
            System.err.print("UnknownHostException when creating listening server: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.print("Unknown Exception creating listening server: " + e.getMessage());
            System.exit(-1);
        }
        server.start();
        while (!this.masterStop) {
            this.bridge.execute(poll());
        }
        this.server.halt();
        this.bridge.shutdown();
    }
    
    public int getThreadNum() {
        return this.threadNum;
    }
    
    public void halt() {
        this.masterStop = true;
    }
}
