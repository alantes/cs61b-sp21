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
    public static final File STAGING_DIR = join(OBJECTS_DIR, "staging");
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

        HEADS_DIR.mkdirs();
        REMOTES_DIR.mkdirs();
        STAGING_DIR.mkdirs();
        BLOBS_DIR.mkdirs();
        COMMITS_DIR.mkdirs();

        HashMap<String, String> stagingMap = new HashMap<>();

        Commit rootCommit = new Commit("initial commit", new Date(0), null);
        String rootCommitRef = rootCommit.saveCommit();

        saveBranch("master", rootCommitRef);
        saveHEAD(rootCommitRef);

        saveStagingMap(stagingMap); // save void map
    }

    public static void addToStaging(String fileName) {
        if (!(Utils.join(Repository.CWD, fileName).exists())) {
            System.out.println("File does not exist");
            return;
        }
        Commit currentCommit = Commit.getCommitFromHashCode(getHEAD());
        String hashCodeInCommit = currentCommit.searchFileHashCodeInCommit(fileName);
        String fileHashCode = calculateFileHashCode(fileName);
        HashMap<String, String> stagingMap = getStagingMap();
        if (fileHashCode == hashCodeInCommit) {
            if (stagingMap.containsKey(fileName)) {
                deleteFromStaging(stagingMap.get(fileName));
                stagingMap.remove(fileName);
            }
            return;
        } else {
            if (stagingMap.containsKey(fileName) && fileHashCode != stagingMap.get(fileName)) { // 待 add 的文件发生了变化，删掉原有的blob和map中的项
                deleteFromStaging(stagingMap.get(fileName));
                stagingMap.remove(fileName);
            } else if (stagingMap.containsKey(fileName)) { // 待 add 的文件原样存在于 stagingMap 中直接返回
                return;
            }
            // 文件不存在于 stagingMap 中
            stagingMap.put(fileName, fileHashCode);
            saveStagingMap(stagingMap);
            // add file to staging area
            saveFileToStaging(fileName);
        }
    }

    /** helper methods that have to be public in order to be utilized by other classes. */
    public static void saveStagingMap(HashMap stagingMap) {
        Utils.writeObject(INDEX, stagingMap);
    }

    public static HashMap<String, String> getStagingMap() {
        return Utils.readObject(INDEX, HashMap.class);
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

    public static String calculateFileHashCode(String fileName) {
        byte[] fileBytes = loadFileContent(fileName);
        return Utils.sha1(fileBytes);
    }

    public static boolean moveFileFromStagingToBlobs(String fileName) {

    }

    /** helper methods. */
    private static void saveFileToStaging(String fileName) {
        byte[] fileContent = loadFileContent(fileName);
        String fileHashCode = calculateFileHashCode(fileName);
        File f = join(STAGING_DIR, fileHashCode);
        Utils.writeContents(f, fileContent);
    }

    /* 需要注意的是一旦文件进入到 blobs 中之后就不再会被删除了，删除只会发生在 staging 中 */
    private static boolean deleteFromStaging(String fileHashCode) { // Utils 中的 restrictedDelete 只能用于删除主文件夹中的文件，不适合删除 staging 中的文件
        File f = join(STAGING_DIR, fileHashCode);
        if (!f.isDirectory()) {
            return f.delete();
        } else {
            return false;
        }
    }


    /** helper method for helper method*/
    private static byte[] loadFileContent(String fileName) {
        File f = Utils.join(CWD, fileName);
        if (!(f.exists())) {
            throw new RuntimeException(
                    String.format("File: %s does not exist in CWD!", fileName));
        }
        return Utils.readContents(f);
    }
}
