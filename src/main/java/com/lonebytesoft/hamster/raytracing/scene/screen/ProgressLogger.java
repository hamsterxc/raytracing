package com.lonebytesoft.hamster.raytracing.scene.screen;

import org.slf4j.Logger;

class ProgressLogger {

    private final Logger logger;
    private final long total;
    private final long messagesCount;

    public ProgressLogger(final Logger logger, final long total, final long messagesCount) {
        this.logger = logger;
        this.total = total;
        this.messagesCount = messagesCount;
    }

    public void log(final long progress) {
        final double part = (double) progress / total;
        final long trigger = (long) Math.ceil(Math.floor(part * messagesCount) / messagesCount * total);
        if (progress == trigger) {
            logger.debug("{}%... {}/{}", Math.round(part * 100), progress, total);
        }
    }

}
