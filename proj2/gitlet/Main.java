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
        switch (cmd) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs("init", args, 1);
                Repository.setupPersistence();
                break;
            case "add":
                // handle the `add [filename]` command
                validateNumArgs("add", args, 2);
                String fileNameToAdd = args[1];
                Repository.addToStaging(fileNameToAdd);
                break;
            case "commit":
                // handle the `commit [message]` command
                validateNumArgs("commit", args, 2);
                String message = args[1];
                Repository.generateNewCommit(message);
                break;
            case "rm":
                // handle the `rm [filename]` command
                validateNumArgs("rm", args, 2);
                String fileNameToRemove = args[1];
                Repository.removeFromTracking(fileNameToRemove);
                break;
            case "log":
                // handle the `log` command
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            case "global-log":
                // handle the `log` command
                validateNumArgs("global-log", args, 1);
                Repository.globalLog();
                break;
            case "checkout":
                if (args.length == 2) {
                    String brachName = args[1];
                    Repository.checkoutBranch(brachName);
                    break;
                }
                if (args.length == 3) {
                    String fileName = args[2];
                    Repository.checkoutFileInHEAD(fileName);
                    break;
                }
                if (args.length == 4) {
                    String commitID = args[1];
                    String fileName = args[3];
                    Repository.checkoutFileInCommit(commitID, fileName);
                    break;
                }
                throw new RuntimeException(
                        String.format("Invalid number of arguments for: %s.", cmd));

            case "find":
                validateNumArgs("find", args, 2);
                String messageToFind = args[1];
                Repository.find(messageToFind);
                break;

            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;

            case "branch":
                validateNumArgs("branch", args, 2);
                String branchName = args[1];
                Repository.createBranch(branchName);
                break;

            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                String branchNameToRemove = args[1];
                Repository.removeBranch(branchNameToRemove);
                break;

            case "reset":
                validateNumArgs("reset", args, 2);
                String commitID = args[1];
                Repository.reset(commitID);
                break;

            default:
                exitWithError(String.format("Unknown command: %s", args[0]));
        }
    }

    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }

    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(-1);
    }
}
