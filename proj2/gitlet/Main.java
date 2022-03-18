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
                // handle the `add [filename]` command
                validateNumArgs("init", args, 2);
                String fileNameToAdd = args[1];
                Repository.addToStaging(fileNameToAdd);
                break;
            case "commit":
                // handle the `commit [message]` command
                validateNumArgs("init", args, 2);
                String message = args[1];
                Repository.generateNewCommit(message);
                break;
            case "rm":
                // handle the `rm [filename]` command
                validateNumArgs("init", args, 2);
                String fileNameToRemove = args[1];
                Repository.removeFromTracking(fileNameToRemove);
                break;
            case "log":
                // handle the `log` command
                validateNumArgs("init", args, 1);
                Repository.log();
                break;
            case "global-log":
                // handle the `log` command
                validateNumArgs("init", args, 1);
                Repository.globalLog();
                break;
        }
    }

    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
