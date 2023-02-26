package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The objects file under .gitlet storing blobs and commits. */
    public static final File OBJ_DIR = join(GITLET_DIR,"objects");


    /* TODO: fill in the rest of this class. */
    public static void init() {
        if (GITLET_DIR.exists()) {
            Utils.exitWithMessage("A Gitlet version-control system already exists in the current directory.");
            return;
        }

        GITLET_DIR.mkdir();
        OBJ_DIR.mkdir();
        Branch.BRANCH_DIR.mkdir();
        Commit.COMMIT_DIR.mkdir();
        Blob.BLOB_DIR.mkdir();
        Stage stage = new Stage();
        stage.save();
        Head.setCurrentBranch("master");
        Commit initialCommit = new Commit();
        Branch.setCommitId("master",initialCommit.getCommitId());

        initialCommit.save();
    }

    public static void add(String fileName) {
        Stage stage = Stage.load();
        System.out.println("add "+stage.getAdditionBlobs());
        File newFileName = Utils.join(CWD,fileName);
        if (!newFileName.exists()) {
            Utils.exitWithMessage("File does not exist.");
        }
        Blob newBlob = new Blob(fileName);
        String blobId = newBlob.getBlobId();
        //Staging an already staged file,rewrite it
        if (stage.getAdditionBlobs().containsKey(fileName)) {
            stage.removeAdditionBlob(fileName);
        }
        //Comparing to the current commit's file, if identical,return
        //Get current commit head->branch->commitId
        Commit currentCommit = Commit.getCommit(Branch.getCommitId(Head.getCurrentBranch()));
        if (blobId.equals(currentCommit.getBlobs().get(fileName))) {
            return;
        }
        //Cancel removal blob
        if (stage.getRemovalBlobs().contains(fileName)) {
            stage.removeRemovalBlob(fileName);
        }
        stage.stageBlob(newBlob);
        System.out.println("add "+stage.getAdditionBlobs());
        System.out.println("remove" + stage.getRemovalBlobs());
        stage.save();
        newBlob.save();
    }

    public static void commit(String commitMessage) {
        //Get current commit
        String currentCommitId = Branch.getCommitId(Head.getCurrentBranch());
        commit(commitMessage,currentCommitId);
    }
    //Commit helper method
    public static void commit(String commitMessage, String currentCommitId) {
        Stage stage = Stage.load();
        if (commitMessage.isEmpty()) {
            Utils.exitWithMessage("Please enter a commit message.");
        }
        if (stage.getAdditionBlobs().isEmpty() && stage.getRemovalBlobs().isEmpty()) {
            Utils.exitWithMessage("No changes to the commit");
        }
        Commit newCommit = new Commit(commitMessage, currentCommitId);
        //Add new blobs
        for (Map.Entry<String,String> entry : stage.getAdditionBlobs().entrySet()) {
            String fileName = entry.getKey();
            String blobId = entry.getValue();
            newCommit.getBlobs().put(fileName,blobId);
        }
        //Remove blobs
        for (String fileName : stage.getRemovalBlobs()) {
            newCommit.getBlobs().remove(fileName);
        }
        newCommit.setCommitId();
        Branch.setCommitId(Head.getCurrentBranch(),newCommit.getCommitId());
        stage.clear();
        stage.save();
        newCommit.save();
    }

    public static void remove(String fileName) {
        Stage stage = Stage.load();
        //if on stage for addition, remove it
        if (stage.getAdditionBlobs().containsKey(fileName)) {
            stage.removeAdditionBlob(fileName);
            stage.save();
            return;
        }
        //if in current commit, stage it for removal and remove it
        Commit currentCommit = Commit.getCommit(Branch.getCommitId(Head.getCurrentBranch()));

        if(currentCommit.getBlobs().containsKey(fileName)) {
            stage.removeBlob(fileName);
            stage.save();
            Utils.join(CWD,fileName).delete();
            return;
        }
        Utils.exitWithMessage("No reason to remove the file");
    }
    public static void log() {
        String commitId = Branch.getCommitId(Head.getCurrentBranch());
        Commit commit = Commit.getCommit(commitId);
        while (!commit.getParents().isEmpty()) {
            System.out.println("===");
            System.out.println("commit " + commitId);
            //Merging condition
            if (commit.getParents().size() == 2) {
                System.out.println("Merge: " + commit.getParents().get(0).substring(0,7) + " " + commit.getParents().get(1).substring(0,7));
            }
            System.out.println("Date: " + commit.getCommitTime());
            System.out.println(commit.getMessage());
            System.out.println();
            commitId = commit.getParents().get(0);
            commit = Commit.getCommit(commitId);
        }
        //Initial Commit
        System.out.println("===");
        System.out.println("commit " + commitId);
        System.out.println("Date: " + commit.getCommitTime());
        System.out.println(commit.getMessage());
        System.out.println();
    }
    //Redundant code, waiting for optimizing
    public static void glog() {
        List<String> commitList = Utils.plainFilenamesIn(Commit.COMMIT_DIR);
        Commit commit;
        for (String id : commitList) {
            commit = Commit.getCommit(id);
            System.out.println("===");
            System.out.println("commit " + id);
            //Merging condition
            if (commit.getParents().size() == 2) {
                System.out.println("Merge: " + commit.getParents().get(0).substring(0,7) + " " + commit.getParents().get(1).substring(0,7));
            }
            System.out.println("Date: " + commit.getCommitTime());
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }
    public static void find(String commitMessage) {
        List<String> commitList = Utils.plainFilenamesIn(Commit.COMMIT_DIR);
        List<String> idList = new ArrayList<>();
        Commit commit;
        assert commitList != null;
        for(String id : commitList) {
            commit = Commit.getCommit(id);
            assert commit != null;
            if (commit.getMessage().equals(commitMessage)) {
                idList.add(id);
            }
        }
        if (idList.isEmpty()) {
            System.out.println("Found no commit with that message.");
        }
        else {
            for (String id : idList) {
                System.out.println(id);
            }
        }
    }

    public static void status() {
        System.out.println("=== Branches ===");
        for(String branchName : Utils.plainFilenamesIn(Branch.BRANCH_DIR)) {
            if (branchName.equals(Head.getCurrentBranch())) {
                System.out.println("*" + branchName);
            }
            else {
                System.out.println(branchName);
            }
        }
        System.out.println();
        System.out.println();
        Stage stage = Stage.load();
        System.out.println("=== Staged Files ===");
        for (Map.Entry<String,String> entry : stage.getAdditionBlobs().entrySet()) {
            String fileName = entry.getKey();
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String fileName : stage.getRemovalBlobs()) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println();
        System.out.println("=== Modification Not Staged For Commit ===");
        System.out.println();
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
        System.out.println();
    }
}
