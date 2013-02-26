package rpictrl.Control;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Bridge extends Thread {
    private static int BridgeThreadCount = 0;
    final static GpioController mainCtrl = GpioFactory.getInstance();
    
    private int threadNum;
    
    final GpioPinDigitalOutput L_FORWARD_PIN = Bridge.mainCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Pin 18", PinState.LOW);
    final GpioPinDigitalOutput L_BACKWARD_PIN = Bridge.mainCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Pin 22", PinState.LOW);
    final GpioPinDigitalOutput R_FORWARD_PIN = Bridge.mainCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_10, "Pin 24", PinState.LOW);
    final GpioPinDigitalOutput R_BACKWARD_PIN = Bridge.mainCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_11, "Pin 26", PinState.LOW);
    
    final Controller leftCtrl = new Controller(this.L_FORWARD_PIN, this.L_BACKWARD_PIN);
    final Controller rightCtrl = new Controller(this.R_FORWARD_PIN, this.R_BACKWARD_PIN);
    
    private static final int STD_PULSE_TIME = 1000;
    private static final int STD_PULSE_UNIT = 100;
    int x=0, y=0, speed=1;
    boolean masterStop = false;    
    
    public Bridge(String str) {
        super(str);
        this.threadNum = Bridge.BridgeThreadCount;
        Bridge.BridgeThreadCount++;
    }
    
    public Bridge() {
        super("BridgeThread-" + Bridge.BridgeThreadCount);
        this.threadNum = Bridge.BridgeThreadCount;
        Bridge.BridgeThreadCount++;
    }
    
    @Override
    public void run() {
        long loop_count = 0;
        
        try {
            while(!this.masterStop) { // Loop while masterStop has not been triggered.
                if (loop_count > 2000000) loop_count = 0; // Reset Loop if number is too large.

                if (this.y != 0) { // Vehicle is accelerating
                    if (this.y > 0) { // Vehicle is moving forward
                        if (this.x != 0) { // Vehicle is turning
                            if (this.x > 0) { // Right
                                this.forwardRight(loop_count);
                            } else { // Left
                                this.forwardLeft(loop_count);
                            } // End forward Direction State
                        } else { // Straight
                            this.forward(loop_count);
                        } // End forward Turning State
                    } else { // Vehicle is moving backward
                        if (this.x != 0) { // Vehicle is turning.
                            if (this.x > 0) { // Right
                                this.backwardRight(loop_count);
                            } else { // Left
                                this.backwardLeft(loop_count);
                            } // End backward Direction State
                        } else { // Straight
                            this.backward(loop_count);
                        } // End backward Turning State
                    } // End forward/backward Acceleration State
                } else { // Vehicle is not accelerating
                    if (this.x != 0) { // Vehicle is turning sharply
                        if (this.x > 0) { // Sharp Right
                            this.sharpForwardRight(loop_count);
                        } else { // Sharp Left
                            this.sharpForwardLeft(loop_count);
                        } // End Stationary Direction State
                    } else { // Stationary
                        this.stopMovement();
                    } // End Stationary Turning State
                } // End Acceleration State
                loop_count++;
            } 
        } catch (Exception e) {
            System.err.println("DriveException on Thread " + this.threadNum + ": " + e.getMessage());
            System.exit(-1);
        }
    }
    
    private void forwardRight(long loop_count) {
        if (loop_count % this.speed == 0) {
            this.leftCtrl.forward();
        } else {
            this.leftCtrl.stop();
        }
        if (loop_count % (this.speed + 1) == 0) {
            this.rightCtrl.forward();
        } else {
            this.rightCtrl.stop();
        }
    }
    
    private void forwardLeft(long loop_count) {
        if (loop_count % this.speed == 0) {
            this.rightCtrl.forward();
        } else {
            this.rightCtrl.stop();
        }
        if (loop_count % (this.speed + 1) == 0) {
            this.leftCtrl.forward();
        } else {
            this.leftCtrl.stop();
        }
    }
    
    private void backwardRight(long loop_count) {
        if (loop_count % this.speed == 0) {
            this.leftCtrl.forward();
        } else {
            this.leftCtrl.stop();
        }
        if (loop_count % this.speed + 1 == 0) {
            this.rightCtrl.forward();
        } else {
            this.rightCtrl.stop();
        }
    }
    
    private void backwardLeft(long loop_count) {
        if (loop_count % this.speed == 0) {
            this.rightCtrl.forward();
        } else {
            this.rightCtrl.stop();
        }
        if (loop_count % this.speed + 1 == 0) {
            this.leftCtrl.forward();
        } else {
            this.leftCtrl.stop();
        }
    }
    
    private void forward(long loop_count) {
        if (loop_count % this.speed == 0) {
            this.leftCtrl.forward();
            this.rightCtrl.forward();
        } else {
            this.leftCtrl.stop();
            this.rightCtrl.stop();
        }
    }
    
    private void backward(long loop_count) {
        if (loop_count % this.speed == 0) {
            this.leftCtrl.forward();
            this.rightCtrl.forward();
        } else {
            this.leftCtrl.stop();
            this.rightCtrl.stop();
        }
    }
    
    private void sharpForwardRight(long loop_count) {
        if (loop_count % this.speed == 0) {
            this.leftCtrl.forward();
        } else {
            this.leftCtrl.stop();
        }
        if (loop_count % this.speed + 1 == 0) {
            this.rightCtrl.forward();
        } else {
            this.rightCtrl.stop();
        }
    }
    
    private void sharpForwardLeft(long loop_count) {
        if (loop_count % this.speed == 0) {
            this.rightCtrl.forward();
        } else {
            this.rightCtrl.stop();
        }
        if (loop_count % this.speed + 1 == 0) {
            this.leftCtrl.forward();
        } else {
            this.rightCtrl.stop();
        }
    }
    
    private void stopMovement() {
        this.leftCtrl.stop();
        this.rightCtrl.stop();
    }
    
    public void setMasterStop(boolean masterStop) {
        this.masterStop = masterStop;
    }
    
    public boolean isMasterStop() {
        return this.masterStop;
    }
    
    public void shutdown() {
        mainCtrl.shutdown();
    }
    
    public boolean isShutdown() {
        return mainCtrl.isShutdown();
    }
}
