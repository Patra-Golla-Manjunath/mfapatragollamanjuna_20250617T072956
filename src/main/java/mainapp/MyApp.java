package mainapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyApp {

    // Method to check if the specified branches are created
    public static String areBranchesCreated() {
        try {
            System.out.println("Checking if branches blog-feature and comment-feature exist...");

            // Get the list of branches using 'git branch'
            String branches = executeCommand("git branch").trim();

            // Check if blog-feature exists in the branch list
            boolean isBlogFeaturePresent = branches.contains("blog-feature");

            // Check if comment-feature exists in the branch list
            boolean isCommentFeaturePresent = branches.contains("comment-feature");

            // Return whether both branches are present
            if (isBlogFeaturePresent && isCommentFeaturePresent) {
                return "true"; // Both branches are created
            } else {
                return "false"; // One or more branches are missing
            }

        } catch (Exception e) {
            System.out.println("Error in areBranchesCreated method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if a branch was created from the specified base branch
    public static String wasBranchCreatedFromBaseBranch(String baseBranch, String branchName) {
        try {
            System.out.println("Checking if branch '" + branchName + "' was created from '" + baseBranch + "'...");

            // Get the reflog to check the history of branch checkouts
            String reflog = executeCommand("git reflog").trim();

            // Search for the entry indicating that the branch was created from the base branch
            String searchTerm = "checkout: moving from " + baseBranch + " to " + branchName;
            Pattern pattern = Pattern.compile(searchTerm);
            Matcher matcher = pattern.matcher(reflog);

            // Check if the reflog contains the relevant entry
            if (matcher.find()) {
                return "true"; // Branch was created from the base branch
            } else {
                return "false"; // Branch was not created from the base branch
            }

        } catch (Exception e) {
            System.out.println("Error in wasBranchCreatedFromBaseBranch method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if a commit message exists in a branch's history
    public static String isCommitPresentInBranch(String branchName, String commitMessage) {
        try {
            System.out.println("Checking if commit with message '" + commitMessage + "' is present in branch '" + branchName + "'...");

            // Execute 'git log' to get the commit history for the specified branch
            String logOutput = executeCommand("git log " + branchName + " --oneline").trim();
            System.out.println("Log Output for branch " + branchName + ":");

            // Create the regex pattern to match the commit message
            String regex = ".*" + Pattern.quote(commitMessage) + ".*";  // Escape special characters in commitMessage
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(logOutput);

            // Check if the commit message matches any line in the log
            if (matcher.find()) {
                return "true";  // Commit is found in the branch
            } else {
                return "false";  // Commit is not found in the branch
            }

        } catch (Exception e) {
            System.out.println("Error in isCommitPresentInBranch method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if a fast-forward merge has occurred using git reflog
    public static String isFastForwardMerge(String branchName) {
        try {
            System.out.println("Checking if a fast-forward merge has occurred in branch '" + branchName + "'...");

            // Execute 'git reflog' to get the history of the branch
            String reflogOutput = executeCommand("git reflog " + branchName).trim();
            System.out.println("Reflog Output for branch " + branchName + ":");

            // Search for the "merge" entry to check if it was a fast-forward merge
            Pattern fastForwardPattern = Pattern.compile("merge.*Fast-forward", Pattern.CASE_INSENSITIVE);
            Matcher fastForwardMatcher = fastForwardPattern.matcher(reflogOutput);

            // If we find a fast-forward merge entry, return true
            if (fastForwardMatcher.find()) {
                return "true";  // Fast-forward merge found
            } else {
                return "false"; // No fast-forward merge found
            }

        } catch (Exception e) {
            System.out.println("Error in isFastForwardMerge method: " + e.getMessage());
            return "";
        }
    }

    // // Method to check if there are merge conflicts in index.html
    // public static String isMergeConflict() {
    //     try {
    //         System.out.println("Checking if there is a merge conflict in 'index.html'...");

    //         // Execute 'git diff' to check if there are conflicts in the working directory
    //         String diffOutput = executeCommand("git diff index.html").trim();
    //         String statusOutput = executeCommand("git status").trim();

    //         // Check if 'git diff' contains merge conflict markers (i.e., '<<<<<<<', '======', '>>>>>>')
    //         Pattern conflictPattern = Pattern.compile("^(<<<<<<<|=======|>>>>>>)", Pattern.MULTILINE);
    //         Matcher conflictMatcher = conflictPattern.matcher(diffOutput);

    //         // Check if there are any conflict markers in 'git diff'
    //         if (conflictMatcher.find()) {
    //             return "true"; // Merge conflict exists in the file
    //         }

    //         // Check if 'git status' shows the file as having conflicts
    //         if (statusOutput.contains("both modified")) {
    //             return "true"; // Merge conflict exists in the file
    //         }

    //         return "false"; // No merge conflict found in 'index.html'

    //     } catch (Exception e) {
    //         System.out.println("Error in isMergeConflict method: " + e.getMessage());
    //         return "";
    //     }
    // }
    // Method to check if there are merge conflicts in 'index.html'
    public static String isMergeConflict() {
        try {
            System.out.println("Checking if there is a merge conflict in 'index.html'...");

            // Path to the index.html file
            File file = new File("index.html");

            // Read the content of index.html
            StringBuilder fileContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            reader.close();

            // Convert the content to a String
            String content = fileContent.toString();

            // Define regex patterns to detect merge conflict markers
            Pattern conflictPattern = Pattern.compile("^(<<<<<<<|=======|>>>>>>)", Pattern.MULTILINE);
            Matcher conflictMatcher = conflictPattern.matcher(content);

            // If any conflict markers are found, it indicates a merge conflict
            if (conflictMatcher.find()) {
                return "true"; // Merge conflict exists in the file
            }

            // If no conflict markers are found, return false
            return "false"; // No merge conflict found in 'index.html'

        } catch (IOException e) {
            System.out.println("Error reading the 'index.html' file: " + e.getMessage());
            return "";
        }
    }

    // Helper method to execute git commands
    private static String executeCommand(String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(".")); // Ensure this is the correct directory where Git repo is located
        processBuilder.command("bash", "-c", command);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitVal = process.waitFor();
        if (exitVal == 0) {
            return output.toString();
        } else {
            System.out.println("Command failed with exit code: " + exitVal);
            throw new RuntimeException("Failed to execute command: " + command);
        }
    }

    // Main method to run the checks manually (can be used to test individually)
    public static void main(String[] args) {
        try {
            // Checking if the branches are created
            String branchesExist = areBranchesCreated();
            if (branchesExist.equals("true")) {
                System.out.println("Branches blog-feature and comment-feature exist.");
            } else {
                System.out.println("One or more branches are missing.");
            }

            // Checking if the branches are created from the correct base branch (main)
            String blogFeatureCreatedFromMain = wasBranchCreatedFromBaseBranch("main", "blog-feature");
            String commentFeatureCreatedFromMain = wasBranchCreatedFromBaseBranch("main", "comment-feature");

            System.out.println("Blog-feature created from main: " + blogFeatureCreatedFromMain);
            System.out.println("Comment-feature created from main: " + commentFeatureCreatedFromMain);

        } catch (Exception e) {
            System.out.println("Error in main method: " + e.getMessage());
        }
    }
}