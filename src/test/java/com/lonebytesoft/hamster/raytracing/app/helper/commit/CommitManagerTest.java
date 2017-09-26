package com.lonebytesoft.hamster.raytracing.app.helper.commit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommitManagerTest {

    private static CommitManager commitManager;

    @BeforeClass
    public static void beforeClass() {
        commitManager = new CommitManager(
                "f3a51e59c93659890154d4e572e039df265cbc89 8fa6759f1c028a8b86917851f0933494a4b827e6\n" +
                "8fa6759f1c028a8b86917851f0933494a4b827e6 cf3695dfe862d9eea13b09857676ba61c6131a22\n" +
                "cf3695dfe862d9eea13b09857676ba61c6131a22 5ee682daad055912803245eee040e1969a7e9769 a79c22be2b09411bb86f222c92be89f7eecabc33\n" +
                "5ee682daad055912803245eee040e1969a7e9769 9dde51ce31af29d20235f14b71e1c580eb23b396\n" +
                "9dde51ce31af29d20235f14b71e1c580eb23b396\n" +
                "a79c22be2b09411bb86f222c92be89f7eecabc33"
        );
    }

    @Test
    public void testNoParents() {
        Assert.assertFalse(commitManager.isNewerOrSame(
                "9dde51ce31af29d20235f14b71e1c580eb23b396",
                "a79c22be2b09411bb86f222c92be89f7eecabc33"));
    }

    @Test
    public void testNoParentsRevert() {
        Assert.assertFalse(commitManager.isNewerOrSame(
                "a79c22be2b09411bb86f222c92be89f7eecabc33",
                "9dde51ce31af29d20235f14b71e1c580eb23b396"));
    }

    @Test
    public void testImmediateParent() {
        Assert.assertTrue(commitManager.isNewerOrSame(
                "5ee682daad055912803245eee040e1969a7e9769",
                "9dde51ce31af29d20235f14b71e1c580eb23b396"));
    }

    @Test
    public void testImmediateParentReverse() {
        Assert.assertFalse(commitManager.isNewerOrSame(
                "9dde51ce31af29d20235f14b71e1c580eb23b396",
                "5ee682daad055912803245eee040e1969a7e9769"));
    }

    @Test
    public void testSeveralParents() {
        Assert.assertTrue(commitManager.isNewerOrSame(
                "cf3695dfe862d9eea13b09857676ba61c6131a22",
                "a79c22be2b09411bb86f222c92be89f7eecabc33"));
    }

    @Test
    public void testSeveralParentsReverse() {
        Assert.assertFalse(commitManager.isNewerOrSame(
                "a79c22be2b09411bb86f222c92be89f7eecabc33",
                "cf3695dfe862d9eea13b09857676ba61c6131a22"));
    }

    @Test
    public void testFar() {
        Assert.assertTrue(commitManager.isNewerOrSame(
                "f3a51e59c93659890154d4e572e039df265cbc89",
                "9dde51ce31af29d20235f14b71e1c580eb23b396"));
    }

    @Test
    public void testFarReverse() {
        Assert.assertFalse(commitManager.isNewerOrSame(
                "9dde51ce31af29d20235f14b71e1c580eb23b396",
                "f3a51e59c93659890154d4e572e039df265cbc89"));
    }

}
