package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    //Directory that saves all blobs.
    public static final File BLOB_DIR = Utils.join(Repository.OBJ_DIR, "blobs");
    //Unique Blob id.
    private String blobId;
    //
    private String fileName;
    //Contents in the file.
    private String fileContent;

    public Blob(String fileName) {
        this.fileName = fileName;
        this.fileContent = Utils.readContentsAsString(Utils.join(Repository.CWD, fileName));
        this.blobId = Utils.sha1(this.fileContent);
    }

    public String getBlobId() {
        return blobId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
    }
    //Save blobContent in BLOB_DIR
    public void save() {
        File newBlob = Utils.join(BLOB_DIR, blobId);
        Utils.writeObject(newBlob, this.fileContent);
    }
}
