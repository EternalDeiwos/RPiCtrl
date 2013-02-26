package rpictrl.Control;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.PriorityQueue;
import java.util.Queue;
import rpictrl.socket.CommandServer;

public class CommandHandler extends Thread {
    public static int threadCount = 0;
    private int threadNum;
    boolean masterStop = false;
    
    Bridge bridge;
    
    Queue<String> commandQueue = new PriorityQueue<String>();
    
    CommandServer server;
    
    public CommandHandler() {
        super("CommandHandler-" + CommandHandler.threadCount);
        this.threadNum = CommandHandler.threadCount;
        CommandHandler.threadCount++;
    }
    
    @Override
    public void run() {
        try {
//            bridge = new Bridge();
            server = new CommandServer(InetAddress.getLocalHost().getHostAddress(), 53789, commandQueue);
        } catch (UnknownHostException e) {
            System.err.print("UnknownHostException when creating listening server: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.print("Unknown Exception creating listening server: " + e.getMessage());
            System.exit(-1);
        }
        server.start();
//        bridge.start();
        while (!this.masterStop) {
            if (!commandQueue.isEmpty()) {
                String current = this.commandQueue.poll();
                System.out.println(current);
                switch (Integer.parseInt(current)) {
                    case Command.FORWARD:
                        this.bridge.y = 1;
                        break;
                    case Command.BACKWARD:
                        this.bridge.y = -1;
                        break;
                    case Command.LEFT:
                        this.bridge.x = -1;
                        break;
                    case Command.RIGHT:
                        this.bridge.x = 1;
                        break;
                    case Command.SPEED_DOWN:
                        if (this.bridge.speed > 1) {
                            this.bridge.speed--;
                        } else {
                            System.err.println("Error decreasing speed: speed is at minimum");
                        }
                        break;
                    case Command.SPEED_UP:
                        if (this.bridge.speed < 5) {
                            this.bridge.speed++;
                        } else {
                            System.err.println("Error increasing speed: speed is at maximum");
                        }
                        break;
                    case Command.STOP:
                        this.bridge.y = 0;
                        break;
                    case Command.CENTER:
                        this.bridge.x = 0;
                        break;
                    default: System.err.println("Unknown Command"); break;
                }
            }
        }
    }
    
    public boolean addQueuedCommand(String command) {
        return this.commandQueue.add(command);
    }
    
    public int getThreadNum() {
        return this.threadNum;
    }
    
    public void halt() {
        this.masterStop = true;
    }
}
