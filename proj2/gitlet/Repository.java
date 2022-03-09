package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here
import java.util.Date;
import java.util.HashMap;

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

    private static HashMap<String, String> pointers;

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");

    /* TODO: fill in the rest of this class. */
    public static void setupPersistence() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdirs();
        COMMITS_DIR.mkdirs();
        BLOBS_DIR.mkdirs();

        Commit rootCommit = new Commit("initial commit", new Date(0));
        String rootCommitHashCode = rootCommit.saveCommit();
        Repository.pointers.put("HEAD", rootCommitHashCode);
        Repository.pointers.put("master", rootCommitHashCode);
        Repository.savePointers();
    }

    public static void savePointers() {
        File f = Utils.join(GITLET_DIR, "pointers");
        Utils.writeObject(f, pointers);
    }


}
