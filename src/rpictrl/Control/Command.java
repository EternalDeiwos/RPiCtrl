package rpictrl.Control;

import java.io.Serializable;

public class Command implements Serializable {
    private ThrustCommand thrust;
    private DirectionCommand direction;
    private SpeedCommand speed;
    private MiscCommand misc;
    
    public Command(ThrustCommand t, DirectionCommand d, SpeedCommand s, MiscCommand m) {
        thrust = t;
        direction = d;
        speed = s;
        misc = m;
    }
    
    public Command() {
        // Default Constructor
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Command) {
            Command cmd = (Command) obj;
            if (this.thrust == cmd.getThrustCommand()) {
                if (this.direction == cmd.getDirectionCommand()) {
                    if (this.speed == cmd.getSpeedCommand()) {
                        if (this.misc == cmd.getMiscCommand()) {
                            return true;
                        }
                    }
                }
            }
        } return false;
    }
    
    public MiscCommand getMiscCommand() {
        return misc;
    }
    
    public ThrustCommand getThrustCommand() {
        return thrust;
    }
    
    public DirectionCommand getDirectionCommand() {
        return direction;
    }
    
    public SpeedCommand getSpeedCommand() {
        return speed;
    }
    
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("THRUST:").append(thrust.toString()).append("\n");
        ret.append("DIRECTION:").append(direction.toString()).append("\n");
        ret.append("SPEED:").append(speed.toString()).append("\n");
        return ret.toString();
    }
    
    public enum MiscCommand {
        QUIT, NONE
    }
    
    public enum ThrustCommand {
        OFF, FORWARD, BACKWARD, NONE;
    }
    
    public enum DirectionCommand {
        CENTER, LEFT, RIGHT, NONE;
    }
    
    public enum SpeedCommand {
        UP, DOWN, NONE;
    }
    
    public class CommandCreator {
        private ThrustCommand creator_thrust;
        private DirectionCommand creator_direction;
        private SpeedCommand creator_speed;
        private MiscCommand creator_misc;
        
        public CommandCreator() {
            creator_thrust = ThrustCommand.NONE;
            creator_direction = DirectionCommand.NONE;
            creator_speed = SpeedCommand.NONE;
            creator_misc = MiscCommand.NONE;
        }

        public ThrustCommand getThrust() {
            return this.creator_thrust;
        }

        public void setThrust(ThrustCommand thrust) {
            this.creator_thrust = thrust;
        }

        public DirectionCommand getDirection() {
            return this.creator_direction;
        }

        public void setDirection(DirectionCommand direction) {
            this.creator_direction = direction;
        }

        public SpeedCommand getSpeed() {
            return this.creator_speed;
        }

        public void setSpeed(SpeedCommand speed) {
            this.creator_speed = speed;
        }

        public MiscCommand getMisc() {
            return this.creator_misc;
        }

        public void setMisc(MiscCommand misc) {
            this.creator_misc = misc;
        }
        
        public Command getCommand() {
            return new Command(this.creator_thrust, this.creator_direction, this.creator_speed, this.creator_misc);
        }
    }
}
