package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please input valid arguments!");
            System.exit(-1);
        }

        String cmd = args[0];
        switch(cmd) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs("init", args, 1);
                Repository.setupPersistence();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs("init", args, 2);
                String fileName = args[1];
                Repository.addToStaging(fileName);
                break;
            // TODO: FILL THE REST IN
        }
    }

    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
