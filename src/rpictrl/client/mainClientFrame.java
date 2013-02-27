package rpictrl.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;
import rpictrl.Control.Command;

public class mainClientFrame extends JFrame implements KeyListener, WindowListener {
    boolean q=false, w=false, e=false, a=false, s=false, d=false;
    
    CommandClient client;
    
    public void run() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JLabel emptyLabel = new JLabel();
        this.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        
        this.setPreferredSize(new Dimension(1000, 1000));
        emptyLabel.setPreferredSize(new Dimension(1000, 1000));
        
        this.pack();
        this.setVisible(true);
        
        try {
            this.client = new CommandClient(InetAddress.getLocalHost().getHostAddress(), 53789);
        } catch (UnknownHostException x) {
            System.err.println("UnknownHostException: " + x.getMessage());
        }
        
        this.addKeyListener(this);
        this.addWindowListener(this);
    }
    
    public void executeCommand(int command) {
        this.client.send(command + "");
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
                } else {
                    executeCommand(Command.STOP);
                }
                this.w = true;
                break;
            case 's': case KeyEvent.VK_DOWN:
                if (!this.w) {
                    executeCommand(Command.BACKWARD);
                } else {
                    executeCommand(Command.STOP);
                }
                this.s = true;
                break;
            case 'd': case KeyEvent.VK_RIGHT:
                if (!this.a) {
                    executeCommand(Command.RIGHT);
                } else {
                    executeCommand(Command.CENTER);
                }
                this.d = true;
                break;
            case 'a': case KeyEvent.VK_LEFT:
                if (!this.d) {
                    executeCommand(Command.LEFT);
                } else {
                    executeCommand(Command.CENTER);
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
                    executeCommand(Command.CENTER);
                } else {
                    executeCommand(Command.LEFT);
                }
                this.d = false;
                break;
            case 'a': case KeyEvent.VK_LEFT:
                if (!this.s) {
                    executeCommand(Command.CENTER);
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
            case 'x':
                this.client.halt();
                this.dispose();
            default: break;
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getWindow().equals(this)) {
            this.client.halt();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }
}
