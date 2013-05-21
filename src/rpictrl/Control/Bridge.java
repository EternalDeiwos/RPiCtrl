package rpictrl.Control;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Bridge {
    final static GpioController mainCtrl = GpioFactory.getInstance();
    
    final GpioPinDigitalOutput L_FORWARD_PIN = Bridge.mainCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Pin 11", PinState.LOW);
    final GpioPinDigitalOutput L_BACKWARD_PIN = Bridge.mainCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Pin 12", PinState.LOW);
    final GpioPinDigitalOutput R_FORWARD_PIN = Bridge.mainCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Pin 13", PinState.LOW);
    final GpioPinDigitalOutput R_BACKWARD_PIN = Bridge.mainCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Pin 15", PinState.LOW);
    
    private Controller leftCtrl;
    private Controller rightCtrl;
    
    private static final int STD_PULSE_TIME = 100;
    private static final int STD_PULSE_UNIT = 50;
    private boolean masterStop = false;
    
    private static int x=0, y=0, s=1;
    
    private Command cmd = new Command(Command.ThrustCommand.OFF, Command.DirectionCommand.CENTER, Command.SpeedCommand.NONE, Command.MiscCommand.NONE);
    
    public Bridge(boolean left, boolean right) {
        if (left) {
            leftCtrl = new Controller(Controller.LEFT, this.L_FORWARD_PIN, this.L_BACKWARD_PIN, STD_PULSE_TIME, STD_PULSE_UNIT);
        }
        if (right) {
            rightCtrl = new Controller(Controller.RIGHT, this.R_FORWARD_PIN, this.R_BACKWARD_PIN, STD_PULSE_TIME, STD_PULSE_UNIT);
        }
    }
    
    public void execute(Command current) {
        if (cmd != current) {
            switch (current.getThrustCommand()) {
                case FORWARD: y = 1; break;
                case BACKWARD: y = -1; break;
                case OFF: y = 0; break;
                case NONE: break;
            }
            switch (current.getDirectionCommand()) {
                case CENTER: x = 0; break;
                case LEFT: x = -1; break;
                case RIGHT: x = 1; break;
                case NONE: break;
            }
            switch (current.getSpeedCommand()) {
                case UP: s++; break;
                case DOWN: s--; break;
                case NONE: break;
            }
            switch (current.getMiscCommand()) {
                case QUIT: shutdown(); break;
                case NONE: break;
            }
        }
        cmd = current;
        //System.out.println(x + " " + y);
        execute();
    }
    
    private void execute() {
        if (y != 0) { // Vehicle is accelerating
            if (y > 0) { // Vehicle is moving forward
                if (x != 0) { // Vehicle is turning
                    if (x > 0) { // Right
                        if (leftCtrl != null) {
                            this.leftCtrl.forward((long)(leftCtrl.getInterval() * 0.8));
                        }
                        if (rightCtrl != null) {
                            this.rightCtrl.forward((long)(rightCtrl.getInterval() * 0.5));
                        }
                    } else { // Left
                        if (leftCtrl != null) {
                            this.leftCtrl.forward((long)(leftCtrl.getInterval() * 0.5));
                        }
                        if (rightCtrl != null) {
                            this.rightCtrl.forward((long)(rightCtrl.getInterval() * 0.8));
                        }
                    } // End forward Direction State
                } else { // Straight
                    if (leftCtrl != null) {
                        this.leftCtrl.forward();
                    }
                    if (rightCtrl != null) {
                        this.rightCtrl.forward();
                    }
                } // End forward Turning State
            } else { // Vehicle is moving backward
                if (x != 0) { // Vehicle is turning.
                    if (x > 0) { // Right
                        if (leftCtrl != null) {
                            this.leftCtrl.backward((long)(leftCtrl.getInterval() * 0.8));
                        }
                        if (rightCtrl != null) {
                            this.rightCtrl.backward((long)(rightCtrl.getInterval() * 0.5));
                        }
                    } else { // Left
                        if (leftCtrl != null) {
                        this.leftCtrl.backward((long)(leftCtrl.getInterval() * 0.5));
                        }
                        if (rightCtrl != null) {
                            this.rightCtrl.backward((long)(rightCtrl.getInterval() * 0.8));
                        }
                    } // End backward Direction State
                } else { // Straight
                    if (leftCtrl != null) {
                        leftCtrl.backward();
                    }
                    if (rightCtrl != null) {
                        rightCtrl.backward();
                    }
                } // End backward Turning State
            } // End forward/backward Acceleration State
        } else { // Vehicle is not accelerating
            if (leftCtrl != null) {
                this.leftCtrl.halt();
            }
            if (rightCtrl != null) {
                this.rightCtrl.halt();
            }
//            if (x != 0) { // Vehicle is turning sharply
//                if (x > 0) { // Sharp Right
//                    if (leftCtrl != null) {
//                        this.leftCtrl.forward((long)(leftCtrl.getInterval() * 0.3));
//                    }
//                    if (rightCtrl != null) {
//                        this.rightCtrl.backward((long)(rightCtrl.getInterval() * 0.3));
//                    }
//                } else { // Sharp Left
//                    if (leftCtrl != null) {
//                        this.leftCtrl.forward((long)(leftCtrl.getInterval() * 0.3));
//                    }
//                    if (rightCtrl != null) {
//                        this.rightCtrl.backward((long)(rightCtrl.getInterval() * 0.3));
//                    }
//                } // End Stationary Direction State
//            } else { // Stationary
//                System.out.println("Stationary");
//                if (leftCtrl != null) {
//                    this.leftCtrl.halt();
//                }
//                if (rightCtrl != null) {
//                    this.rightCtrl.halt();
//                }
//            } // End Stationary Turning State
        } // End Acceleration State
    }
    
    public void setMasterStop(boolean masterStop) {
        this.masterStop = masterStop;
    }
    
    public boolean isMasterStop() {
        return this.masterStop;
    }
    
    public void shutdown() {
        L_FORWARD_PIN.setState(PinState.LOW);
        L_BACKWARD_PIN.setState(PinState.LOW);
        R_FORWARD_PIN.setState(PinState.LOW);
        R_BACKWARD_PIN.setState(PinState.LOW);
        mainCtrl.shutdown();
    }
    
    public boolean isShutdown() {
        return mainCtrl.isShutdown();
    }
}
