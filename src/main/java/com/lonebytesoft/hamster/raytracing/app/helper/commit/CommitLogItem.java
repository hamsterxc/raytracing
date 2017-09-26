package com.lonebytesoft.hamster.raytracing.app.helper.commit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CommitLogItem {

    private static final String PATTERN_STRING_HASH = "[0-9a-fA-F]{40}";
    private static final Pattern PATTERN_LINE = Pattern.compile("\\s*" + PATTERN_STRING_HASH + "(\\s+" + PATTERN_STRING_HASH + ")*\\s*");
    private static final Pattern PATTERN_HASH = Pattern.compile("(" + PATTERN_STRING_HASH + ")");

    private final String hash;
    private final List<String> parents;

    public CommitLogItem(final String line) {
        if(!PATTERN_LINE.matcher(line).matches()) {
            throw new RuntimeException("Could not parse log line: '" + line + "'");
        }

        final Matcher matcher = PATTERN_HASH.matcher(line);
        final List<String> parents = new ArrayList<>();
        if(matcher.find()) {
            this.hash = sanitize(matcher.group(1));

            while(matcher.find()) {
                parents.add(sanitize(matcher.group(1)));
            }
        } else {
            throw new RuntimeException("Could not parse log line: '" + line + "'");
        }
        this.parents = Collections.unmodifiableList(parents);
    }

    private String sanitize(final String input) {
        return input.trim().toLowerCase();
    }

    public String getHash() {
        return hash;
    }

    public List<String> getParents() {
        return parents;
    }

}
