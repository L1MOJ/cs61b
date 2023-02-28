package gitlet;

import java.util.Date;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    //Todo: validate number of args?
    public static void main(String[] args) {
        // TODO: what if args is empty?
        validateEmptyArgs(args);

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateInit();
                validateArgsNum(args,2);
                Repository.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                validateInit();
                validateArgsNum(args,2);
                Repository.commit(args[1]);
                break;
            case "rm":
                validateInit();
                validateArgsNum(args,2);
                Repository.remove(args[1]);
                break;
            case "log":
                validateInit();
                validateArgsNum(args,1);
                Repository.log();
                break;
            case "global-log":
                validateInit();
                validateArgsNum(args,1);
                Repository.glog();
                break;
            case "find":
                validateInit();
                validateArgsNum(args,2);
                Repository.find(args[1]);
                break;
            case "status":
                validateInit();
                validateArgsNum(args,1);
                Repository.status();
                break;
            case "checkout":
                Repository.checkout(args);
                break;
            case "branch":
                validateInit();
                validateArgsNum(args,2);
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                validateInit();
                validateArgsNum(args,2);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                validateInit();
                validateArgsNum(args,2);
                Repository.reset(args[1]);
                break;
            case "merge":
                validateInit();
                validateArgsNum(args,2);
                Repository.merge(args[1]);
                break;
            default:
                Utils.exitWithMessage("No command with that name exists.");
        }
    }
    //Validate empty command
    private static void validateEmptyArgs(String[] args) {
        if(args.length == 0) {
            Utils.exitWithMessage("Please enter a command.");
        }
    }
    //Validate init
    private static void validateInit() {
        if (!Repository.GITLET_DIR.exists()) {
            Utils.exitWithMessage("Not in an initialized Gitlet directory.");
        }
    }
    //Validate Args Num
    private static void validateArgsNum(String[] args, int num) {
        if (args.length != num) {
            Utils.exitWithMessage("Incorrect operands.");
        }
    }
}
