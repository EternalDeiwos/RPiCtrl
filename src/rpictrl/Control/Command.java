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
            default: return Command.ERROR;
        }
    }
}
