package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

import java.util.Formatter;

import java.io.File;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Ryan Yao
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /* TODO: fill in the rest of this class. */
    private Date commitDate;
    private String currentCommitHashCode;
    private String parent;
    private String secondParent;
    private String branch;

    /** The List that store all the hash codes of files. */
    private HashMap<String, String> files = new HashMap<>();

    public Commit(String message, Date commitDate, String parent, String branch) {
        this.commitDate = commitDate;
        this.message = message;
        this.parent = parent;
        this.branch = branch;

        if (this.getParent() != null) {
            Commit parentCommit = Commit.getCommitFromHashCode(getParent());
            this.files = parentCommit.files;

            HashMap<String, String> stagingMap = Repository.getStagingMap();

            for (String fileName : stagingMap.keySet()) {
                if (stagingMap.get(fileName).equals("removed")) { // 两个字符串判断相等与否要用 equals()
                    this.files.remove(fileName);
                    stagingMap.remove(fileName);
                } else {
                    files.put(fileName, stagingMap.get(fileName));
                    Repository.moveFileFromStagingToBlobs(stagingMap.get(fileName));
                    stagingMap.remove(fileName);
                }
            }
            Repository.saveStagingMap(stagingMap);
        }
    }

    public String getBranch() {
        return this.branch;
    }

    public String toString() {
        StringBuilder returnSB = new StringBuilder("===\n");
        returnSB.append(String.format("commit %s\n", getCommitHashCode()));
        if (getSecondParent() != null) {
            returnSB.append(String.format("Merge: %.7s %.7s\n", getParent(), getSecondParent()));
        }
        returnSB.append(String.format("Date: %s\n", getCommitDate()));
        returnSB.append(String.format("%s\n", getMessage()));
        return returnSB.toString();
    }

    public static Commit getCommitFromHashCode(String commitHashCode) {
        File f = Utils.join(Repository.COMMITS_DIR, commitHashCode);
        if (f.exists()) {
            Commit c = Utils.readObject(f, Commit.class);
            return c;
        } else {
            return null;
        }
    }

    public String saveCommit() {
        String commitHashCode = this.getCommitHashCode();
        File f = Utils.join(Repository.COMMITS_DIR, commitHashCode);
        Utils.writeObject(f, this);
        return commitHashCode;
    }

    public String getParent() {
        return parent;
    }

    public String getSecondParent() {
        return secondParent;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public String getMessage() {
        return message;
    }

    public String getCommitHashCode() {
        if (currentCommitHashCode == null) {
            currentCommitHashCode = Utils.sha1(Utils.serialize(this)); // 需要先将 object 转换为 byte[]
        }
        return currentCommitHashCode;
    }

    public String searchFileHashCodeInCommit(String fileName) {
        if (files.containsKey(fileName)) {
            return files.get(fileName);
        } else {
            return null;
        }
    }

    public HashMap<String, String> getFiles() {
        return files;
    }
}
