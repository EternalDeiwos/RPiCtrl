package rpictrl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import rpictrl.Control.CommandHandler;
import rpictrl.client.mainClientFrame;


public class introFrame extends JFrame implements ActionListener {
    JButton server, client;
    CommandHandler handler;
    mainClientFrame clientFrame;
    
    public void run() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.server = new JButton();
        this.client = new JButton();
        
        this.server.setText("Server");
        this.client.setText("Client");
        
        this.server.setActionCommand("server");
        this.client.setActionCommand("client");
        
        this.server.addActionListener(this);
        this.client.addActionListener(this);
        
        this.getContentPane().add(this.server, BorderLayout.NORTH);
        this.getContentPane().add(this.client, BorderLayout.SOUTH);
        
        this.pack();
        this.setVisible(true);
    }
    
    public void startServer() {
        this.handler = new CommandHandler();
        this.handler.start();
        this.dispose();
    }
    
    public void startClient() {
        this.clientFrame = new mainClientFrame();
        this.clientFrame.run();
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if ("server".equals(ae.getActionCommand())) {
            startServer();
        } else {
            startClient();
        }
    }
}
