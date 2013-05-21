package rpictrl;

public class RPiCtrl {

    public static void main(String[] args) {
        try {
            if (args[args.length-1].equals("-s")) {
                introFrame.startServer();
            } else {
                introFrame mainframe = new introFrame();
                mainframe.run();
            }
        } catch (Exception e) {
            System.err.println("No Commandline Parameters Detected");
            introFrame mainframe = new introFrame();
            mainframe.run();
        }
        
    }
}
