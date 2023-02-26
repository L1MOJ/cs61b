package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    public static final File COMMIT_DIR = Utils.join(Repository.OBJ_DIR,"commits");
    /** The message of this Commit. */
    private String message;
    /** The parents(sha1 code) of this Commit. */
    private List<String> parents;
    /** The Commit Date and its String format. */
    private Date currentTime;
    private String commitTime;
    /** The blobs this Commit contains. */
    private HashMap<String,String> blobs;
    /** The CommitID. */
    private String commitId;
    public Commit(String commitMessage,String currentCommitId) {
        this.message = commitMessage;
        this.parents = new ArrayList<>();
        this.parents.add(currentCommitId);
        this.currentTime = new Date();
        this.commitTime = dateNormalization(currentTime);
        Commit currentCommit = Commit.getCommit(currentCommitId);
        this.blobs = new HashMap<>();
        this.blobs.putAll(currentCommit.blobs);

    }
    public Commit() {
        this.message = "initial commit";
        this.currentTime = new Date(0);
        this.commitTime = dateNormalization(currentTime);
        this.parents = new ArrayList<>();
        this.blobs = new HashMap<>();
        this.commitId = Utils.sha1(commitTime,message,parents.toString(),blobs.toString());
    }
    //Get commit by Id
    public static Commit getCommit(String commitId) {
        for(String id: Utils.plainFilenamesIn(COMMIT_DIR)) {
            if (id.equals(commitId)) {
                File targetCommit = Utils.join(COMMIT_DIR,id);
                return Utils.readObject(targetCommit,Commit.class);
            }
        }
        return null;
    }
    //May not be useful, use getCommitTime() to get the String format of Time
    public Date getCurrentTime() {
        return currentTime;
    }

    //Return Commit time as String
    public String getCommitTime() {
        return commitTime;
    }
    private String dateNormalization(Date currentTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        return sdf.format(currentTime);
    }
    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
        this.commitTime = dateNormalization(currentTime);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    public void addBlobs(HashMap<String, String> blobs) {
        this.blobs = blobs;
    }

    public String getCommitId() {
        return commitId;
    }
    public void setCommitId() {
        this.commitId = Utils.sha1(this.commitTime,this.message,this.parents.toString(),this.blobs.toString());
    }

    //Save current commit to objects file
    public void save() {
        File newCommit = Utils.join(COMMIT_DIR,this.commitId);
        Utils.writeObject(newCommit,this);
    }
}
