package gitlet;

import java.io.File;

public class Head {
    public static final File HEAD_FILE = Utils.join(Repository.GITLET_DIR, "HEAD");

    //Get current branchName head is pointing to.
    public static String getCurrentBranch() {
        String branchName = Utils.readContentsAsString(HEAD_FILE);
        return branchName;
    }
    //Set head to another branch
    public static void setCurrentBranch(String branchName) {
        Utils.writeContents(HEAD_FILE, branchName);
    }
}
