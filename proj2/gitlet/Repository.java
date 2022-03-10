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

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");

    public static final File HEADS_DIR = join(REFS_DIR, "heads"); // heads 文件夹中装 branches 对应的 tip 指针
    public static final File REMOTES_DIR = join(REFS_DIR, "remotes"); // remotes 文件夹中装 repo 文件夹，repo 文件夹中装不同 repo 的对应 tip 指针


    public static final File INDEX = join(GITLET_DIR, "index"); // a map stands for staging area
    public static final File HEAD = join(GITLET_DIR, "HEAD");


    /* TODO: fill in the rest of this class. */
    public static void setupPersistence() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }

        REFS_DIR.mkdirs();
        OBJECTS_DIR.mkdirs();
        BLOBS_DIR.mkdirs();
        COMMITS_DIR.mkdirs();

        HashMap<String, String> stagingMap = new HashMap<>();

        Commit rootCommit = new Commit("initial commit", new Date(0), null);
        String rootCommitRef = rootCommit.saveCommit();

        saveBranch("master", rootCommitRef);
        saveHEAD(rootCommitRef);

        saveINDEX(stagingMap); // save void map
    }

    public static void addToStaging(String fileName) {
        if (!(Utils.join(Repository.CWD, fileName).exists())) {
            System.out.println("File not exist!");
            return;
        }
        Commit currentCommit = Commit.commitFromHash(getHEAD());
        String hashCodeInCommit = currentCommit.searchHashInCommit(fileName);
        String fileHashCode = getFileHashCode(fileName);
        if (fileHashCode == hashCodeInCommit) {
            return;
        } else {
            HashMap<String, String> stagingMap = getStagingMap();
            stagingMap.put(fileName, fileHashCode);
            saveINDEX(stagingMap);
            // add file to blobs
            saveFileToBlobs(fileName);
        }
    }


    public static void saveHEAD(String commitRef) {     // 和原本 git 相比有所简化，不对master/非 master的commit做区分，只存储commit的reference
        Utils.writeContents(HEAD, commitRef);
    }

    public static String getHEAD() {     // 和原本 git 相比有所简化，不对master/非 master的commit做区分，只存储commit的reference
        return Utils.readContentsAsString(HEAD);
    }

    public static void saveBranch(String branchName, String commitRef) {     // 和原本 git 相比有所简化，不对master/非 master的commit做区分，只存储commit的reference
        File f = join(HEADS_DIR, branchName);
        Utils.writeContents(f, commitRef);
    }
    
    public static String getFileHashCode(String fileName) {
        byte[] fileBytes = loadFile(fileName);
        return Utils.sha1(fileBytes);
    }

    public static byte[] loadFile(String fileName) {
        File f = Utils.join(CWD, fileName);
        return Utils.readContents(f);
    }

    public static void saveFileToBlobs(String fileName) {
        byte[] fileContent = loadFile(fileName);
        String fileHashCode = getFileHashCode(fileName);
        File f = join(BLOBS_DIR, fileHashCode);
        Utils.writeContents(f, fileContent);
    }

    public static void saveINDEX(HashMap stagingMap) {
        Utils.writeObject(INDEX, stagingMap);
    }

    public static HashMap getStagingMap() {
        return Utils.readObject(INDEX, HashMap.class);
    }
    
}
