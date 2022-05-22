package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here
import java.util.*;

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
    public static final File REMOTES_DIR = join(REFS_DIR, "remotes");
    // remotes 文件夹中装 repo 文件夹，repo 文件夹中装不同 repo 的对应 tip 指针


    public static final File INDEX = join(GITLET_DIR, "index"); // a map stands for staging area
    public static final File HEAD = join(GITLET_DIR, "HEAD");


    /* TODO: fill in the rest of this class. */
    public static void setupPersistence() {
        if (GITLET_DIR.exists()) {
            System.out.println(
                    "A Gitlet version-control system already exists in the current directory.");
            return;
        }

        HEADS_DIR.mkdirs();
        REMOTES_DIR.mkdirs();
        STAGING_DIR.mkdirs();
        BLOBS_DIR.mkdirs();
        COMMITS_DIR.mkdirs();

        HashMap<String, String> stagingMap = new HashMap<>();

        Commit rootCommit = new Commit("initial commit", new Date(0), null, "master");
        String rootCommitHashCode = rootCommit.saveCommit();

        saveBranch(rootCommit.getBranch(), rootCommitHashCode);
        saveHEAD(rootCommit.getBranch()); // saveHEAD should be at the back of saveBranch

        saveStagingMap(stagingMap); // save void map
    }

    public static void addToStaging(String fileName) {
        if (!(Utils.join(Repository.CWD, fileName).exists())) {
            System.out.println("File does not exist");
            return;
        }
        Commit currentCommit = Commit.getCommitFromHashCode(getHEADAsCommitID());
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
            if (stagingMap.containsKey(fileName) && fileHashCode != stagingMap.get(fileName)) {
                // 待 add 的文件发生了变化，删掉原有的blob和map中的项
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

    public static void createBranch(String branchName) {
        if (Utils.join(HEADS_DIR, branchName).exists()){
            System.out.println("A branch with that name already exists.");
            return;
        }
        String headCommitID = getHEADAsCommitID();
        saveBranch(branchName, headCommitID);
    }


    public static boolean removeBranch(String branchNameToRemove) {
        File f = Utils.join(HEADS_DIR, branchNameToRemove);
        if (!(f.exists())){
            System.out.println("A branch with that name does not exist.");
            return false;
        }
        if (getHEAD().equals(branchNameToRemove)){
            System.out.println("Cannot remove the current branch.");
            return false;
        }
        return f.delete();
    }

    public static void generateNewCommit(String message) {
        if (getStagingMap().size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        if (message == null) {
            System.out.println("Please enter a commit message.");
            return;
        }
        Commit currentCommit = Commit.getCommitFromHashCode(getHEADAsCommitID());
        Commit newCommit = new Commit(message, new Date(), getHEADAsCommitID(), currentCommit.getBranch());
        String newCommitHashCode = newCommit.saveCommit();
        saveBranch(newCommit.getBranch(), newCommitHashCode);
    }

    public static void removeFromTracking(String fileName) {
        Commit currentCommit = Commit.getCommitFromHashCode(getHEADAsCommitID());
        HashMap<String, String> stagingMap = getStagingMap();
        if (stagingMap.containsKey(fileName)) {
            // 应该是 unstage 优先，如果同时在 staging 和 current commit 中应该会只 unstage
            stagingMap.remove(fileName);
            deleteFromStaging(calculateFileHashCode(fileName));
        } else if (currentCommit.getFiles().containsKey(fileName)) {
            stagingMap.put(fileName, "removed");
            Utils.restrictedDelete(fileName); // remove the file from the working directory
        } else {
            System.out.println("No reason to remove the file.");
        }
        Repository.saveStagingMap(stagingMap);
    }

    public static void checkoutFileInHEAD(String fileName) {
        checkoutFileInCommit(getHEADAsCommitID(), fileName);
    }

    private static String searchCommitCodeFromID(String commitID) {
        List<String> allCommitList = Utils.plainFilenamesIn(COMMITS_DIR);
        if (commitID.length() == 40) {
            if (allCommitList.contains(commitID)) {
                return commitID;
            } else {
                return null;
            }
        }
        for (String aCommitHashCode : allCommitList) {
            if (aCommitHashCode.startsWith(commitID)) {
                return aCommitHashCode;
            }
        }
        return null;
    }

    public static void reset(String targetCommitID) {
        String targetCommitHashCode = searchCommitCodeFromID(targetCommitID);
        if (targetCommitHashCode == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        if (checkUntrackedFiles()) {
            return;
        }
        // files manipulation: Checks out all the files tracked by the given commit.
        //      Removes tracked files that are not present in that commit.
        Commit currentCommit = Commit.getCommitFromHashCode(getHEADAsCommitID());
        Commit targetCommit = Commit.getCommitFromHashCode(targetCommitHashCode);
        checkoutCommit(currentCommit, targetCommit);

        //  moves the current branch’s head to that commit node
        String currentBranchName = currentCommit.getBranch();
        saveBranch(currentBranchName, targetCommitHashCode);
    }

    public static void checkoutFileInCommit(String commitID, String fileName) {
        // search commit hashcode using commit id
        String commitHashCode = searchCommitCodeFromID(commitID);
        if (commitHashCode == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit theCommit = Commit.getCommitFromHashCode(commitHashCode);
        Map<String, String> fileMap = theCommit.getFiles();
        moveFileFromBlobsToCWD(fileName, fileMap.get(fileName));
    }

    private static boolean checkIfFileInBlobs(String fileName) {
        String fileHashCode = calculateFileHashCode(fileName);
        File f = Utils.join(BLOBS_DIR, fileHashCode);
        return f.exists();
    }

    private static boolean checkIfFileInCommit(String fileName, String commitHashCode) {
        Commit theCommit = Commit.getCommitFromHashCode(commitHashCode);
        String fileHashCode = calculateFileHashCode(fileName);
        Map<String, String> commitFileMap = theCommit.getFiles();
        return (commitFileMap.containsKey(fileName)
                && commitFileMap.get(fileName).equals(fileHashCode));
    }

    private static void checkoutCommit(Commit currentCommit, Commit targetCommit) {
        Map<String, String> targetFileMap = targetCommit.getFiles();
        Map<String, String> currentCommitFileMap = currentCommit.getFiles();

        for (String currentCommitFile : currentCommitFileMap.keySet()) {
            Utils.restrictedDelete(currentCommitFile);
        }
        for (String branchCommitFile : targetFileMap.keySet()) {
            moveFileFromBlobsToCWD(branchCommitFile, currentCommitFileMap.get(branchCommitFile));
        }

        List<String> allStagingFileList = Utils.plainFilenamesIn(STAGING_DIR);
        for (String p : allStagingFileList) {
            deleteFromStaging(p);
        }
        saveStagingMap(new HashMap<>());

    }

    private static boolean checkUntrackedFiles() {
        List<String> allCWDFileList = Utils.plainFilenamesIn(CWD);
        for (String fileName : allCWDFileList) {
            if (!checkIfFileInCommit(fileName, getHEADAsCommitID())) {
                System.out.println(fileName);
                System.out.println(
                        "There is an untracked file in the way; "
                                + "delete it, or add and commit it first.");
                return true;
            }
        }
        return false;
    }

    public static void checkoutBranch(String branchName) {
        if (checkUntrackedFiles()) {
            return;
        }
        String branchHeadHashCode = getBranchHead(branchName);
        if (branchHeadHashCode == null) {
            System.out.println("No such branch exists.");
            return;
        }
        Commit branchCommit = Commit.getCommitFromHashCode(branchHeadHashCode);
        Commit currentCommit = Commit.getCommitFromHashCode(getHEADAsCommitID());
        if (branchCommit.getBranch().equals(currentCommit.getBranch())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        saveHEAD(branchName);
        checkoutCommit(currentCommit, branchCommit);

    }

    public static void log() {
        String p = getHEADAsCommitID();
        while (p != null) {
            Commit commitPointer = Commit.getCommitFromHashCode(p);
            System.out.println(commitPointer);
            p = commitPointer.getParent();
        }
    }

    public static void globalLog() {
        List<String> allCommitList = Utils.plainFilenamesIn(COMMITS_DIR);
        for (String p : allCommitList) {
            Commit commitPointer = Commit.getCommitFromHashCode(p);
            System.out.println(commitPointer);
        }
    }

    public static void find(String messageToFind) {
        List<String> allCommitList = Utils.plainFilenamesIn(COMMITS_DIR);
        int nullFoundIndicator = 1;
        for (String p : allCommitList) {
            Commit commitPointer = Commit.getCommitFromHashCode(p);
            if (commitPointer.getMessage().contains(messageToFind)) {
                System.out.println(p);
                nullFoundIndicator = 0;
            }
        }
        if (nullFoundIndicator == 1) {
            System.out.println("Found no commit with that message.");
        }
    }

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }

    public static void status() {
        StringBuilder returnSB = new StringBuilder("=== Branches ===\n");

        List<String> allBranchList = Utils.plainFilenamesIn(HEADS_DIR);
        allBranchList.sort(new StringComparator());
        for (String s : allBranchList) {
            if (s.equals(getHEAD())) {
                returnSB.append("*");
            }
            returnSB.append(String.format("%s\n",s));
        }
        returnSB.append("\n");

        Map<String, String> stagingMap = getStagingMap();
        List<String> allStagedList = new ArrayList<>();
        List<String> allRemovedList = new ArrayList<>();
        for (String s : stagingMap.keySet()) {
            if (stagingMap.get(s).equals("removed")) {
                allRemovedList.add(s);
            } else {
                allStagedList.add(s);
            }
        }
        allRemovedList.sort(new StringComparator());
        allStagedList.sort(new StringComparator());

        returnSB.append("=== Staged Files ===\n");
        for (String s : allStagedList) {
            returnSB.append(String.format("%s\n",s));
        }
        returnSB.append("\n");

        returnSB.append("=== Removed Files ===\n");
        for (String s : allRemovedList) {
            returnSB.append(String.format("%s\n",s));
        }
        returnSB.append("\n");
        returnSB.append("=== Modifications Not Staged For Commit ===\n");
        returnSB.append("\n");
        returnSB.append("=== Untracked Files ===\n");
        returnSB.append("\n");
        System.out.println(returnSB);
    }

    /** helper methods that have to be public in order to be utilized by other classes. */
    public static void saveStagingMap(HashMap stagingMap) {
        Utils.writeObject(INDEX, stagingMap);
    }

    public static HashMap<String, String> getStagingMap() {
        return Utils.readObject(INDEX, HashMap.class);
    }

    // saveHEAD should be used behind saveBranch
    public static void saveHEAD(String commitRefOrBranchName) {
        Utils.writeContents(HEAD, commitRefOrBranchName);
    }

    public static String getHEAD() {
        return Utils.readContentsAsString(HEAD);
    }

    public static String getHEADAsCommitID() {
        String HEADContent = Utils.readContentsAsString(HEAD);
        File f = Utils.join(HEADS_DIR, HEADContent);
        if (f.exists()) {
            return Utils.readContentsAsString(f);
        }
        return Utils.readContentsAsString(HEAD);
    }

    public static void saveBranch(String branchName, String commitRef) {
        File f = join(HEADS_DIR, branchName);
        Utils.writeContents(f, commitRef);
    }

    public static String getBranchHead(String branchName) {
        File f = join(HEADS_DIR, branchName);
        if (!f.exists()) {
            return null;
        }
        return Utils.readContentsAsString(f);
    }

    public static String calculateFileHashCode(String fileName) {
        byte[] fileBytes = loadFileContent(fileName);
        return Utils.sha1(fileBytes);
    }

    public static void moveFileFromStagingToBlobs(String fileHashCode) {
        File fileInStaging = join(STAGING_DIR, fileHashCode);
        File fileInBlobs = Utils.join(BLOBS_DIR, fileHashCode);
        if (fileInBlobs.exists()) {
            deleteFromStaging(fileHashCode);
        } else {
            fileInStaging.renameTo(fileInBlobs);
            deleteFromStaging(fileHashCode);
        }
    }

    private static void moveFileFromBlobsToCWD(String fileName, String fileHashCode) {
        if (fileHashCode == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File fileInBlobs = Utils.join(BLOBS_DIR, fileHashCode);
        File fileInCWD = Utils.join(CWD, fileName);
        String blobContent = Utils.readContentsAsString(fileInBlobs);
        Utils.writeContents(fileInCWD, blobContent);
    }

    /** helper methods. */
    private static void saveFileToStaging(String fileName) {
        byte[] fileContent = loadFileContent(fileName);
        String fileHashCode = calculateFileHashCode(fileName);
        File f = join(STAGING_DIR, fileHashCode);
        Utils.writeContents(f, fileContent);
    }

    /* 需要注意的是一旦文件进入到 blobs 中之后就不再会被删除了，删除只会发生在 staging 中 */
    private static boolean deleteFromStaging(String fileHashCode) {
        // Utils 中的 restrictedDelete 只能用于删除主文件夹中的文件，不适合删除 staging 中的文件
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
