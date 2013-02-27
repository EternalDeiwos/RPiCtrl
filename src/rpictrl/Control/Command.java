package rpictrl.Control;

public class Command {
    public static final int ERROR=0, RIGHT=1, LEFT=2, FORWARD=3, BACKWARD=4, SPEED_UP=5, SPEED_DOWN=6, STOP=7, CENTER=8;
    
    public static int getCommand(char command) {
        switch (command) {
            case 'r': return Command.RIGHT;
            case 'l': return Command.LEFT;
            case 'f': return Command.FORWARD;
            case 'b': return Command.BACKWARD;
            case 'u': return Command.SPEED_UP;
            case 'd': return Command.SPEED_DOWN;
            case 's': return Command.STOP;
            case 'c': return Command.CENTER;
            default: return Command.ERROR;
        }
    }
    
    public static String getCommand(int command) {
        switch (command) {
            case -1: return "SHUTDOWN";
            case 1: return "RIGHT";
            case 2: return "LEFT";
            case 3: return "FORWARD";
            case 4: return "BACKWARD";
            case 5: return "SPEED_UP";
            case 6: return "SPEED_DOWN";
            case 7: return "STOP";
            case 8: return "CENTER";
            default: return "ERROR";
        }
    }
}
