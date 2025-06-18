package com.libraryapp.test.functional;

import static com.libraryapp.test.utils.TestUtils.businessTestFile;
import static com.libraryapp.test.utils.TestUtils.currentTest;
import static com.libraryapp.test.utils.TestUtils.testReport;
import static com.libraryapp.test.utils.TestUtils.yakshaAssert;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import mainapp.MyApp;

public class MainFunctionalTest {

    @AfterAll
    public static void afterAll() {
        testReport();
    }

    @Test
    @Order(1)
    public void testBranchCreation() throws IOException {
        try {
            // Check if branches blog-feature and comment-feature exist
            String branchesOutput = MyApp.areBranchesCreated();

            // Assert that both branches are created
            yakshaAssert(currentTest(), branchesOutput.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(2)
    public void testBranchCreationFromMain() throws IOException {
        try {
            // Check if blog-feature branch was created from main
            String blogFeatureCreatedFromMain = MyApp.wasBranchCreatedFromBaseBranch("main", "blog-feature");

            // Check if comment-feature branch was created from main
            String commentFeatureCreatedFromMain = MyApp.wasBranchCreatedFromBaseBranch("main", "comment-feature");

            // Assert that both branches are created from main
            yakshaAssert(currentTest(), blogFeatureCreatedFromMain.equals("true") && commentFeatureCreatedFromMain.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(3)
    public void testCommitMessageAddingIndexHtml() throws IOException {
        try {
            // Check if "adding index.html file" commit is present in 'main' branch
            String result = MyApp.isCommitPresentInBranch("main", "adding index.html file");

            // Check if "added about us" commit is present in 'main' branch
            String result2 = MyApp.isCommitPresentInBranch("main", "added about us");

            // Check if "added comment section" commit is present in 'comment-feature' branch
            String result3 = MyApp.isCommitPresentInBranch("comment-feature", "added comment section");

            // Check if "added blog feature" commit is present in 'blog-feature' branch
            String result4 = MyApp.isCommitPresentInBranch("blog-feature", "added blog feature");

            // Assert that the commit is present in the 'blog-feature' branch
            yakshaAssert(currentTest(), result.equals("true") && result2.equals("true") && result3.equals("true") && result4.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(4)
    public void testFastForwardMerge() throws IOException {
        try {
            // Check if a fast-forward merge occurred in the 'main' branch
            String result = MyApp.isFastForwardMerge("main");

            // Assert that the result indicates a fast-forward merge (true means fast-forward)
            yakshaAssert(currentTest(), result.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(5)
    public void testMergeConflict() throws IOException {
        try {
            // Check if there is a merge conflict in 'index.html'
            String result = MyApp.isMergeConflict();

            // Assert that the result indicates whether there is a conflict or not
            yakshaAssert(currentTest(), result.equals("true"), businessTestFile); // 'true' means there is a conflict
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile); // If an error occurs, it will fail the test
        }
    }
}