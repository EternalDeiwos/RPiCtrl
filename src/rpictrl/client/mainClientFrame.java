package rpictrl.client;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Queue;
import javax.swing.*;
import rpictrl.Control.Command;

public class mainClientFrame extends JFrame implements KeyListener {
    boolean q=false, w=false, e=false, a=false, s=false, d=false;
    
    CommandClient client;
    Queue<String> commandQueue;
    
    public void run() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel emptyLabel = new JLabel();
        this.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        
        this.setSize(1000, 1000);
        emptyLabel.setSize(1000, 1000);
        
        this.pack();
        this.setVisible(true);
        
        try {
            this.client = new CommandClient(InetAddress.getLocalHost().getHostAddress(), 53789, this.commandQueue);
            this.client.start();
        } catch (UnknownHostException x) {
            System.err.println("UnknownHostException: " + x.getMessage());
        }
    }
    
    public void executeCommand(int command) {
        this.client.commandQueue.add(command + "");
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        // None
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyChar()) {
            case 'w': case KeyEvent.VK_UP:
                if (!this.s) {
                    executeCommand(Command.FORWARD);
                }
                this.w = true;
                break;
            case 's': case KeyEvent.VK_DOWN:
                if (!this.w) {
                    executeCommand(Command.BACKWARD);
                }
                this.s = true;
                break;
            case 'd': case KeyEvent.VK_RIGHT:
                if (!this.a) {
                    executeCommand(Command.RIGHT);
                }
                this.d = true;
                break;
            case 'a': case KeyEvent.VK_LEFT:
                if (!this.d) {
                    executeCommand(Command.LEFT);
                }
                this.a = true;
                break;
            case 'q':
                if (!this.e) {
                    executeCommand(Command.SPEED_DOWN);
                }
                this.q = true;
                break;
            case 'e':
                if (!this.q) {
                    executeCommand(Command.SPEED_UP);
                }
                this.e = true;
                break;
            default: break;
        } 
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        System.out.println(ke.getKeyChar());
        switch (ke.getKeyChar()) {
            case 'w': case KeyEvent.VK_UP:
                if (!this.s) {
                    executeCommand(Command.STOP);
                } else {
                    executeCommand(Command.BACKWARD);
                }
                this.w = false;
                break;
            case 's': case KeyEvent.VK_DOWN:
                if (!this.w) {
                    executeCommand(Command.STOP);
                } else {
                    executeCommand(Command.FORWARD);
                }
                this.s = false;
                break;
            case 'd': case KeyEvent.VK_RIGHT:
                if (!this.s) {
                    executeCommand(Command.STOP);
                } else {
                    executeCommand(Command.LEFT);
                }
                this.d = false;
                break;
            case 'a': case KeyEvent.VK_LEFT:
                if (!this.s) {
                    executeCommand(Command.STOP);
                } else {
                    executeCommand(Command.RIGHT);
                }
                this.a = false;
                break;
            case 'q':
                this.q = false;
                break;
            case 'e':
                this.e = false;
                break;
            default: break;
        }
    }
}
