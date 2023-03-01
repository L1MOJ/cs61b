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
        File newFileName = Utils.join(CWD,fileName);
        if (!newFileName.exists()) {
            Utils.exitWithMessage("File does not exist.");
        }
        Blob newBlob = new Blob(fileName);
        String blobId = newBlob.getBlobId();
        //Comparing to the current commit's file, if identical,return
        //Get current commit head->branch->commitId
        Commit currentCommit = Commit.getCommit(Branch.getCommitId(Head.getCurrentBranch()));
        if (blobId.equals(currentCommit.getBlobs().get(fileName))) {
            stage.removeAdditionBlob(fileName);
            stage.removeRemovalBlob(fileName);
            return;
        }
        //Staging an already staged file,rewrite it
        if (stage.getAdditionBlobs().containsKey(fileName)) {
            stage.removeAdditionBlob(fileName);
        }
        //Cancel removal blob
        if (stage.getRemovalBlobs().contains(fileName)) {
            stage.removeRemovalBlob(fileName);
        }
        stage.stageBlob(newBlob);
        stage.save();
        newBlob.save();
    }

    public static void commit(String commitMessage) {
        //Get current commit
        String currentCommitId = Branch.getCommitId(Head.getCurrentBranch());
        commit(commitMessage,currentCommitId,null);
    }
    //Commit helper method
    public static void commit(String commitMessage, String currentCommitId,String mergedCommitId) {
        Stage stage = Stage.load();
        if (commitMessage.isEmpty()) {
            Utils.exitWithMessage("Please enter a commit message.");
        }
        if (stage.getAdditionBlobs().isEmpty() && stage.getRemovalBlobs().isEmpty()) {
            Utils.exitWithMessage("No changes added to the commit.");
        }
        Commit newCommit = new Commit(commitMessage, currentCommitId,mergedCommitId);
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
            if (commit.getParents().get(1) != null) {
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
            if (commit.getParents().size() == 2 && commit.getParents().get(1) != null) {
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
        Commit currentCommit = Commit.getCommit(Branch.getCommitId(Head.getCurrentBranch()));
        Stage stage = Stage.load();
        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);

        System.out.println("=== Branches ===");
        List<String> branches = Utils.plainFilenamesIn(Branch.BRANCH_DIR);
        Collections.sort(branches);
        System.out.println("*" + Head.getCurrentBranch());
        for(String branchName : branches) {
            if (!branchName.equals(Head.getCurrentBranch())) {
                System.out.println(branchName);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (Map.Entry<String,String> entry : stage.getAdditionBlobs().entrySet()) {
            String fileName = entry.getKey();
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String fileName : stage.getRemovalBlobs()) {
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Modification Not Staged For Commit ===");
        List<String> mnsFile = getMnsFile(stage, currentCommit, cwdFiles);
        for (String fileName : mnsFile) {
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        List<String> untrackedFiles = getUntrackedFiles(stage, currentCommit, cwdFiles);
        for (String fileName : untrackedFiles) {
            System.out.println(fileName);
        }
        System.out.println();
    }
    //Helper method for status-getMnsFiles
    private static List<String> getMnsFile(Stage stage, Commit currentCommit, List<String> cwdFiles) {
        List<String> mnsFiles = new ArrayList<>();
        for (String fileName : cwdFiles) {
            Blob blob = new Blob(fileName);
            boolean tracked = currentCommit.getBlobs().containsKey(fileName);
            boolean modified = !blob.getBlobId().equals(currentCommit.getBlobs().get(fileName));
            boolean staged = stage.getAdditionBlobs().containsKey(fileName);
            //In current commit, modified but not staged
            if (tracked && modified && !staged) {
                mnsFiles.add(fileName + " (modified)");
                continue;
            }
            //Staged for addition but modified later
            modified = !blob.getBlobId().equals(stage.getAdditionBlobs().get(fileName));
            if (modified && staged) {
                mnsFiles.add(fileName + " (moddified)");
            }
        }
        //In current commit, deleted and not staged
        for (String fileName : currentCommit.getBlobs().keySet()) {
            boolean deleted = !cwdFiles.contains(fileName);
            boolean staged = stage.getRemovalBlobs().contains(fileName);
            if (deleted && !staged) {
                mnsFiles.add(fileName + " (deleted)");
            }
        }
        //Staged for addition but deleted
        for (String fileName : stage.getAdditionBlobs().keySet()) {
            boolean deleted = !cwdFiles.contains(fileName);
            if (deleted) {
                mnsFiles.add(fileName + " (deleted)");
            }
        }
        Collections.sort(mnsFiles);
        return mnsFiles;
    }

    //Helper method for status-getUntrackedFiles
    private static List<String> getUntrackedFiles(Stage stage, Commit currentCommit, List<String> cwdFiles) {
        List<String> untracked = new ArrayList<>();
        for (String fileName : cwdFiles) {
            boolean tracked = currentCommit.getBlobs().containsKey(fileName);
            boolean staged = stage.getAdditionBlobs().containsKey(fileName);
            //untracked
            if (!tracked && !staged) {
                untracked.add(fileName);
            }
        }
        Collections.sort(untracked);
        return untracked;
    }

    public static void checkout(String[] args) {
        //checkout --[file name]
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                Utils.exitWithMessage("Incorrect operands.");
            }
            String fileName = args[2];
            checkoutFile(Branch.getCommitId(Head.getCurrentBranch()), fileName);
        }
        //checkout [commit id] --[file name]
        if (args.length == 4) {
            if (!args[2].equals("--")) {
                Utils.exitWithMessage("Incorrect operands.");
            }
            String fileName = args[3];
            String commitId = args[1];
            checkoutFile(commitId, fileName);
        }
        //checkout [branchname]
        if (args.length == 2) {
            String branchName = args[1];
            checkoutBranch(branchName);
        }
    }
    //checkout file helper method
    private static void checkoutFile(String commitId, String fileName) {
        //commitId error
        if (!Utils.plainFilenamesIn(Commit.COMMIT_DIR).contains(commitId)) {
            Utils.exitWithMessage("No commit with that id exists.");
        }
        //fileName doesn't exist
        if (!Commit.getCommit(commitId).getBlobs().containsKey(fileName)) {
            Utils.exitWithMessage("File does not exist in that commit.");
        }
        File checkBlob = Utils.join(Blob.BLOB_DIR,Commit.getCommit(commitId).getBlobs().get(fileName));
        Blob blob = Utils.readObject(checkBlob,Blob.class);
        Utils.writeContents(Utils.join(CWD,fileName),blob.getFileContent());
    }
    //checkout branch helper method
    private static void checkoutBranch(String branchName) {
        //Same branch checkout
        if (branchName.equals(Head.getCurrentBranch())) {
            Utils.exitWithMessage("No need to checkout the current branch.");
        }
        //Invalid branchName
        Commit commit = Commit.getCommit(Branch.getCommitId(branchName));
        if (commit == null) {
            Utils.exitWithMessage("No such branch exists.");
        }
        checkoutCommit(commit);
        //Set HEAD Pointer to the branch
        Head.setCurrentBranch(branchName);

    }

    private static void checkoutCommit(Commit commit) {
        //Delete files that is not tracked by newBranch
        for (String fileName : Utils.plainFilenamesIn(CWD)) {
            File file = Utils.join(CWD,fileName);
            if (!commit.getBlobs().containsKey(fileName)) {
                file.delete();
            }
        }
        //tracked by B but not by A, but already exists in CWD
        for (String fileName : Utils.plainFilenamesIn(CWD)) {
            boolean fileTrackByA = Commit.getCommit(Branch.getCommitId(Head.getCurrentBranch())).getBlobs().containsKey(fileName);
            boolean fileTrackByB = commit.getBlobs().containsKey(fileName);
            if (fileTrackByB && !fileTrackByA) {
                Utils.exitWithMessage("There is an untracked file in the way;delete it, or add and commit it first.");
            }
        }
        //Update files trakced by B
        for (String fileName : commit.getBlobs().keySet()) {
            checkoutFile(commit.getCommitId(), fileName);
        }
        //Clear stage
        Stage stage = Stage.load();
        stage.clear();
        stage.save();
    }

    public static void branch(String branchName) {
        if (Utils.plainFilenamesIn(Branch.BRANCH_DIR).contains(branchName)) {
            Utils.exitWithMessage("A branch with that name already exists");
        }
        Branch.setCommitId(branchName,Branch.getCommitId(Head.getCurrentBranch()));
//        Head.setCurrentBranch(branchName);
    }

    public static void rmBranch(String branchName) {
        if (branchName.equals(Head.getCurrentBranch())) {
            Utils.exitWithMessage("Cannot remove the current branch.");
        }
        for (String branch : Utils.plainFilenamesIn(Branch.BRANCH_DIR)) {
            if (branchName.equals(branch)) {
                File branchFile = Utils.join(Branch.BRANCH_DIR,branchName);
                branchFile.delete();
                return;
            }
        }
        Utils.exitWithMessage("A branch with that name does not exist.");
    }

    public static void reset(String commitId) {
        Commit commit = Commit.getCommit(commitId);
        if (commit == null) {
            Utils.exitWithMessage("No commit with that id exists.");
        }
        checkoutCommit(commit);
        Branch.setCommitId(Head.getCurrentBranch(),commitId);
    }

    public static void merge(String branchName) {
        Stage stage = Stage.load();
        if (!stage.getAdditionBlobs().isEmpty() || !stage.getRemovalBlobs().isEmpty()) {
            Utils.exitWithMessage("You have uncommitted changes.");
        }
        String mergedCommitId = Branch.getCommitId(branchName);
        String currentCommitId = Branch.getCommitId(Head.getCurrentBranch());
        if (mergedCommitId == null) {
            Utils.exitWithMessage("A branch with that name does not exist.");
        }
        Commit mergedCommit = Commit.getCommit(mergedCommitId);
        Commit currentCommit = Commit.getCommit(currentCommitId);

        if (mergedCommitId.equals(currentCommitId)) {
            Utils.exitWithMessage("Cannot merge a branch with itself.");
        }

        //???Maybe error
        for (String fileName : Utils.plainFilenamesIn(CWD)) {
            boolean fileTrackByA = currentCommit.getBlobs().containsKey(fileName);
            boolean fileTrackByB = mergedCommit.getBlobs().containsKey(fileName);
            if (fileTrackByB && !fileTrackByA) {
                Utils.exitWithMessage("There is an untracked file in the way;delete it, or add and commit it first.");
            }
        }
        //Find split point
        String splitPointCommitId = getSplitPoint(currentCommit,mergedCommit);

        if (splitPointCommitId.equals(mergedCommitId)) {
            Utils.exitWithMessage("Given branch is an ancestor of the current branch.");
        }

        if (splitPointCommitId.equals(currentCommitId)) {
            checkoutCommit(mergedCommit);
            Branch.setCommitId(Head.getCurrentBranch(),mergedCommitId);
            Utils.exitWithMessage("Current branch fast-forward.");
        }

        Commit splitCommit = Commit.getCommit(splitPointCommitId);
        boolean isConflict = merging(currentCommit,mergedCommit,splitCommit,stage);
        stage.save();
        String commitMessage = "Merged " + branchName + " into" + Head.getCurrentBranch() + ".";
        commit(commitMessage,currentCommitId,mergedCommitId);
        if (isConflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }
    //Helper method to deal with merging conflicts
    private static void conflictMerge(Stage stage,String fileName,String currentBlobId,String mergedBlobId) {
        String currentContent = "";
        String mergedContent = "";
        if (currentBlobId != null) {
            File currentBlob = Utils.join(Blob.BLOB_DIR,currentBlobId);
            currentContent = Utils.readContentsAsString(currentBlob);
        }
        if (mergedBlobId != null) {
            File mergedBlob = Utils.join(Blob.BLOB_DIR,mergedBlobId);
            mergedContent = Utils.readContentsAsString(mergedBlob);
        }
        File conflictFile = Utils.join(CWD,fileName);
        String conflictContent = "<<<<<<< HEAD\n" + currentContent +  "=======" + mergedContent + ">>>>>>>";
        Blob newBlob = new Blob(fileName);
        newBlob.save();
        stage.getAdditionBlobs().put(fileName,newBlob.getBlobId());
        Utils.writeContents(conflictFile,conflictContent);
    }
    //Remove merged blobs
    private static void removeMergedBlobs(String fileName,HashMap<String,String> currentBlobs,HashMap<String,String> mergedBlobs,HashMap<String,String> splitBlobs) {
        currentBlobs.remove(fileName);
        mergedBlobs.remove(fileName);
        splitBlobs.remove(fileName);
    }
    //Helper method for concrete merging process
    private static boolean merging(Commit currentCommit,Commit mergedCommit,Commit splitCommit,Stage stage) {
        boolean isConflict = false;
        HashMap<String,String> currentBlobs = currentCommit.getBlobs();
        HashMap<String,String> mergedBlobs = mergedCommit.getBlobs();
        HashMap<String,String> splitBlobs = splitCommit.getBlobs();
        for (String fileName : currentBlobs.keySet()) {
            String currentBlobId = currentBlobs.get(fileName);
            String mergedBlobId = mergedBlobs.get(fileName);
            String splitBlobId = splitBlobs.get(fileName);
            // X a !a     Xaa  XaX
            if (splitBlobId == null) {
                if (mergedBlobId != null && !mergedBlobId.equals(currentBlobId)) {
                    conflictMerge(stage,fileName,currentBlobId,mergedBlobId);
                    removeMergedBlobs(fileName,currentBlobs,mergedBlobs,splitBlobs);
                    isConflict = true;
                }
            }
            //a a X->remove /a a a ->stay /a a !a->merge /!a a X->conf  /!a a a->stay /!a a !a->stay
            else {
                if (mergedBlobId == null) {
                    if (splitBlobId.equals(currentBlobId)) {
                        Utils.join(CWD,fileName).delete();
                        stage.getRemovalBlobs().add(fileName);
                    }
                    //conflict
                    else {
                        conflictMerge(stage,fileName,currentBlobId,mergedBlobId);
                        isConflict = true;
                    }
                }
                else {
                    if (splitBlobId.equals(currentBlobId) && !splitBlobId.equals(mergedBlobId)) {
                        checkoutFile(mergedCommit.getCommitId(),fileName);
                        stage.getAdditionBlobs().put(fileName,mergedBlobId);
                    }
                }
            }
        }
        for (String fileName : mergedBlobs.keySet()) {
            String currentBlobId = currentBlobs.get(fileName);
            String mergedBlobId = mergedBlobs.get(fileName);
            String splitBlobId = splitBlobs.get(fileName);
            if (splitBlobId == null) {
                checkoutFile(mergedCommit.getCommitId(),fileName);
                stage.getAdditionBlobs().put(fileName,mergedBlobId);
            }
            else if (!mergedBlobId.equals(splitBlobId))
            {
                conflictMerge(stage,fileName,currentBlobId,mergedBlobId);
                isConflict = true;
            }
        }
        return isConflict;
    }
    //Helper method to get splitpoint
    private static String getSplitPoint(Commit currentCommit, Commit mergedCommit) {
        Deque<String> currentStack = new LinkedList<>();
        Deque<String> mergedStack = new LinkedList<>();
        Commit tempCommit = currentCommit;
        String tempCommitId = "";
        //Won't push Init Commit Id but that's OK?
        while (!tempCommit.getParents().isEmpty()) {
            currentStack.push(tempCommit.getCommitId());
            tempCommit = Commit.getCommit(tempCommit.getParents().get(0));
        }
        tempCommit = mergedCommit;
        while (!tempCommit.getParents().isEmpty()) {
            mergedStack.push(tempCommit.getCommitId());
            tempCommit = Commit.getCommit(tempCommit.getParents().get(0));
        }
        while (currentStack.peek().equals(mergedStack.peek())) {
            tempCommitId = currentStack.peek();
            currentStack.pop();
            mergedStack.pop();
        }
        return tempCommitId;
    }
}
