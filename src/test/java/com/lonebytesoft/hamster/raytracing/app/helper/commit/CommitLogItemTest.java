package com.lonebytesoft.hamster.raytracing.app.helper.commit;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class CommitLogItemTest {

    @Test
    public void testHashOnly() {
        final String hash = "a79c22be2b09411bb86f222c92be89f7eecabc33";
        final String line = buildLine(hash);

        final CommitLogItem item = new CommitLogItem(line);

        Assert.assertEquals(hash, item.getHash());
        Assert.assertEquals(Collections.emptyList(), item.getParents());
    }

    @Test
    public void testOneParent() {
        final String hash = "5ee682daad055912803245eee040e1969a7e9769";
        final String parent = "9dde51ce31af29d20235f14b71e1c580eb23b396";
        final String line = buildLine(hash, parent);

        final CommitLogItem item = new CommitLogItem(line);

        Assert.assertEquals(hash, item.getHash());
        Assert.assertEquals(Collections.singletonList(parent), item.getParents());
    }

    @Test
    public void testTwoParents() {
        final String hash = "cf3695dfe862d9eea13b09857676ba61c6131a22";
        final String parentFirst = "5ee682daad055912803245eee040e1969a7e9769";
        final String parentSecond = "a79c22be2b09411bb86f222c92be89f7eecabc33";
        final String line = buildLine(hash, parentFirst, parentSecond);

        final CommitLogItem item = new CommitLogItem(line);

        Assert.assertEquals(hash, item.getHash());
        Assert.assertEquals(Arrays.asList(parentFirst, parentSecond), item.getParents());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalid() {
        final String hash = "abcdef";
        final String line = buildLine(hash);

        final CommitLogItem item = new CommitLogItem(line);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidEmpty() {
        final String hash = "";
        final String line = buildLine(hash);

        final CommitLogItem item = new CommitLogItem(line);
    }

    private String buildLine(final String hash, final String... parents) {
        final StringBuilder line = new StringBuilder(hash);
        for(final String parent : parents) {
            line.append(' ').append(parent);
        }
        return line.toString();
    }

}
