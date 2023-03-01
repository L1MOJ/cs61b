package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Stage implements Serializable {

//    public static final File ADDITION_DIR = Utils.join(Repository.STAGE_DIR,"addition");
    public static final File STAGE_DIR = Utils.join(Repository.GITLET_DIR, "stage");
    private HashMap<String, String> additionBlobs;
    private HashSet<String> removalBlobs;
    public Stage() {
        additionBlobs = new HashMap<>();
        removalBlobs = new HashSet<>();
    }
    //Put new Blob on addition stage.
    public void stageBlob(Blob newBlob) {
        additionBlobs.put(newBlob.getFileName(), newBlob.getBlobId());
//        File blob = Utils.join(ADDITION_DIR,newBlob.getBlobId());
//        Utils.writeObject(blob,newBlob);
    }
    //Put new Blob on removal stage.
    public void removeBlob(String fileName) {
        removalBlobs.add(fileName);
//        File blob = Utils.join(REMOVAL_DIR,newBlob.getBlobId());
//        Utils.writeObject(blob,newBlob);
    }

    public void removeAdditionBlob(String fileName) {
        additionBlobs.remove(fileName);
    }

    public HashMap<String, String> getAdditionBlobs() {
        return additionBlobs;
    }

    public void removeRemovalBlob(String fileName) {
        removalBlobs.remove(fileName);
    }

    public HashSet<String> getRemovalBlobs() {
        return removalBlobs;
    }
    public void clear() {
        additionBlobs.clear();
        removalBlobs.clear();
    }
    public void save() {
        Utils.writeObject(STAGE_DIR, this);
    }
    public static Stage load() {
        return Utils.readObject(STAGE_DIR, Stage.class);
    }
}
