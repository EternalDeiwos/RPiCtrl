
package rpictrl.Control;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class Controller {
    private GpioPinDigitalOutput forwardPin, backPin;
    
    public Controller(GpioPinDigitalOutput pin1, GpioPinDigitalOutput pin2) {
        this.forwardPin = pin1;
        this.backPin = pin2;
    }
    
    public void forward() {
        this.forwardPin.high();
        this.backPin.low();
    }
    
    public void backward() {
        this.forwardPin.low();
        this.backPin.high();
    }
    
    public void stop() {
        this.forwardPin.low();
        this.backPin.high();
    }
}
