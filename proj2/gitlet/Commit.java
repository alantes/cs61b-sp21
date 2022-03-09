package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.LinkedList;

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
    public String prev;

    /** The List that store all the hash codes of files. */
    private LinkedList<String> files;

    public Commit(String message, Date commitDate) {
        this.commitDate = commitDate;
        this.message = message;
        this.prev = null;
    }

    public static Commit fromFile(String commitHashCode) {
        File f = Utils.join(Repository.COMMITS_DIR, commitHashCode);
        if (f.exists()) {
            Commit c = Utils.readObject(f, Commit.class);
            return c;
        } else {
            return null;
        }
    }

    public String saveCommit() {
        String commitHashCode = this.getHashCode();
        File f = Utils.join(Repository.COMMITS_DIR, commitHashCode);
        Utils.writeObject(f, this);
        return commitHashCode;
    }

    public String getHashCode() {
        return Utils.sha1(this);
    }
}
