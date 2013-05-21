package rpictrl.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import rpictrl.Control.Command;
import rpictrl.Control.Command.CommandCreator;
import rpictrl.onShutdown;

public class mainClientFrame extends JFrame implements KeyListener, WindowListener {
    HashMap<Integer, Boolean> keysDown = new HashMap<>();
    CommandClient client;
    Command cmd = new Command();
    
    public void run() {
        Runtime.getRuntime().addShutdownHook(new onShutdown(this));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JLabel emptyLabel = new JLabel();
        this.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        
        this.setPreferredSize(new Dimension(1000, 1000));
        emptyLabel.setPreferredSize(new Dimension(1000, 1000));
        
        this.pack();
        this.setVisible(true);
        
//        try {
            this.client = new CommandClient("10.0.0.6", 53789);
//        } catch (UnknownHostException x) {
//            System.err.println("UnknownHostException: " + x.getMessage());
//        }
        keysDown.put(KeyEvent.VK_UP, false);
        keysDown.put(KeyEvent.VK_DOWN, false);
        keysDown.put(KeyEvent.VK_LEFT, false);
        keysDown.put(KeyEvent.VK_RIGHT, false);
            
        this.addKeyListener(this);
        this.addWindowListener(this);
    }
    
    public void commandBuilder() {
        CommandCreator builder = cmd.new CommandCreator();
        Set<Map.Entry<Integer, Boolean>> set = keysDown.entrySet();
        for (Map.Entry<Integer, Boolean> entry : set) {
            switch (entry.getKey()) {
                case KeyEvent.VK_UP:
                    if (entry.getValue()) {
                        builder.setThrust(Command.ThrustCommand.FORWARD);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (entry.getValue()) {
                        builder.setThrust(Command.ThrustCommand.BACKWARD);
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (entry.getValue()) {
                        builder.setDirection(Command.DirectionCommand.LEFT);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (entry.getValue()) {
                        builder.setDirection(Command.DirectionCommand.RIGHT);
                    }
                    break;
                default: break;
            }
            if (!keysDown.get(KeyEvent.VK_UP) && !keysDown.get(KeyEvent.VK_DOWN)) builder.setThrust(Command.ThrustCommand.OFF);
            if (!keysDown.get(KeyEvent.VK_LEFT) && !keysDown.get(KeyEvent.VK_RIGHT)) builder.setDirection(Command.DirectionCommand.CENTER);
        }
        cmd = builder.getCommand();
    }
    
    public CommandClient getClient() {
        return client;
    }
    
    public void executeCommand(Command cmd) {
        this.client.send(cmd);
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        // None
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyChar()) {
            case 'w': case KeyEvent.VK_UP:
                keysDown.put(KeyEvent.VK_UP, true);
                break;
            case 's': case KeyEvent.VK_DOWN:
                keysDown.put(KeyEvent.VK_DOWN, true);
                break;
            case 'd': case KeyEvent.VK_RIGHT:
                keysDown.put(KeyEvent.VK_RIGHT, true);
                break;
            case 'a': case KeyEvent.VK_LEFT:
                keysDown.put(KeyEvent.VK_LEFT, true);
                break;
            case 'q':
                executeCommand(new Command(Command.ThrustCommand.NONE, Command.DirectionCommand.NONE, Command.SpeedCommand.UP, Command.MiscCommand.NONE));
                break;
            case 'e':
                executeCommand(new Command(Command.ThrustCommand.NONE, Command.DirectionCommand.NONE, Command.SpeedCommand.DOWN, Command.MiscCommand.NONE));
                break;
            case 'x': case KeyEvent.VK_ESCAPE:
                executeCommand(new Command(Command.ThrustCommand.NONE, Command.DirectionCommand.NONE, Command.SpeedCommand.NONE, Command.MiscCommand.QUIT));
                try {
                    this.client.halt();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
                this.dispose();
            default: break;
        }
        commandBuilder();
        executeCommand(cmd);
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        switch (ke.getKeyChar()) {
            case 'w': case KeyEvent.VK_UP:
                keysDown.put(KeyEvent.VK_UP, false);
                break;
            case 's': case KeyEvent.VK_DOWN:
                keysDown.put(KeyEvent.VK_DOWN, false);
                break;
            case 'd': case KeyEvent.VK_RIGHT:
                keysDown.put(KeyEvent.VK_RIGHT, false);
                break;
            case 'a': case KeyEvent.VK_LEFT:
                keysDown.put(KeyEvent.VK_LEFT, false);
                break;
            default: break;
        }
        commandBuilder();
        executeCommand(cmd);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getWindow().equals(this)) {
            try {
                this.client.halt();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                System.exit(-1);
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        try {
            this.client.halt();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
        this.dispose();
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
