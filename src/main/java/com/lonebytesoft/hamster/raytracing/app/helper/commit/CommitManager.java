package com.lonebytesoft.hamster.raytracing.app.helper.commit;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

public class CommitManager {

    private final Map<String, Collection<String>> commits;

    public CommitManager(final String logLines) {
        final List<CommitLogItem> commitLogItems = Arrays.stream(logLines.split("\n"))
                .map(CommitLogItem::new)
                .collect(Collectors.toList());
        commits = commitLogItems.stream()
                .collect(Collectors.toMap(CommitLogItem::getHash, CommitLogItem::getParents));
        commits.put(null, Collections.singletonList(commitLogItems.get(0).getHash()));
    }

    public boolean isNewerOrSame(final String subjectHash, final String referenceHash) {
        checkCommitKnown(referenceHash);

        final Queue<String> hashes = new LinkedList<>();
        hashes.add(subjectHash);

        while(hashes.size() > 0) {
            final String hash = hashes.poll();
            if(Objects.equals(hash, referenceHash)) {
                return true;
            } else {
                checkCommitKnown(hash);
                hashes.addAll(commits.get(hash));
            }
        }
        return false;
    }

    public boolean isOlder(final String subjectHash, final String referenceHash) {
        return !isNewerOrSame(subjectHash, referenceHash);
    }

    private void checkCommitKnown(final String hash) {
        if(!commits.containsKey(hash)) {
            throw new IllegalStateException("Unknown commit: '" + hash + "'");
        }
    }

}
