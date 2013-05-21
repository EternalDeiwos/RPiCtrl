
package rpictrl.Control;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

public class Controller extends Thread {
    public static final String LEFT = "Controller-Left";
    public static final String RIGHT = "Controller-Right";
    private GpioPinDigitalOutput forwardPin, backPin;
    private long interval, pulse_time;
    
    public Controller(String name, GpioPinDigitalOutput pin1, GpioPinDigitalOutput pin2) {
        super(name);
        this.forwardPin = pin1;
        this.backPin = pin2;
        interval = 1000;
        pulse_time = 500;
    }
    
    public Controller(String name, GpioPinDigitalOutput pin1, GpioPinDigitalOutput pin2, long interval, long pulse_time) {
        super(name);
        this.forwardPin = pin1;
        this.backPin = pin2;
        this.interval = interval;
        this.pulse_time = pulse_time;
    }
    
    public void forward(long pulse_time) {
        if (!(forwardPin.isHigh() && backPin.isLow())) {
            forwardPin.pulse(pulse_time);
            backPin.pulse(pulse_time, PinState.LOW);
            try {
                Thread.sleep(interval - pulse_time);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public void forward() {
        forward(pulse_time);
    }
    
    public void backward(long pulse_time) {
        if (!(forwardPin.isLow() && backPin.isHigh())) {
            forwardPin.pulse(pulse_time, PinState.LOW);
            backPin.pulse(pulse_time);
            try {
                Thread.sleep(interval - pulse_time);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public void backward() {
        backward(pulse_time);
    }
    
    public void halt() {
        this.forwardPin.low();
        this.backPin.low();
    }

    public long getInterval() {
        return interval;
    }
}
