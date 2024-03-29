package gitlet;

import java.io.File;

public class Branch {
    public static final File BRANCH_DIR = Utils.join(Repository.GITLET_DIR, "branches");

    public static void setCommitId(String branchName, String commitId) {
        File branchFile = Utils.join(BRANCH_DIR, branchName);
        Utils.writeContents(branchFile, commitId);
    }

    public static String getCommitId(String branchName) {
        File branchFile = Utils.join(BRANCH_DIR, branchName);
        if (!branchFile.exists()) {
            Utils.exitWithMessage("No such branch exists.");
        }
        return Utils.readContentsAsString(branchFile);
    }
}
