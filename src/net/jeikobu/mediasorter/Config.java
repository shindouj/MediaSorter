package net.jeikobu.mediasorter;

import java.io.File;

/**
 * MediaSorter - created by shindouj on 2017-04-14.
 * Licensed under GPLv3.
 */
public interface Config {
    int copyRetries();
    boolean retryEnabled();
    File filterPath();
    File listenPath();
    File log4jConfigPath();
    long listenInterval();
}
