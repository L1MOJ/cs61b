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
        if(args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                Repository.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                Repository.commit(args[1]);
                break;
            case "rm":
                Repository.remove(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.glog();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;

        }
    }
}
