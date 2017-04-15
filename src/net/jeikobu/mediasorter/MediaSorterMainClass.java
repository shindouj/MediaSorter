package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import net.jeikobu.mediasorter.exceptions.DoesNotBelongToThisCategory;
import net.jeikobu.mediasorter.exceptions.MoreThanOneCategoryException;
import net.jeikobu.mediasorter.exceptions.MoreThanOneSubcategoryException;
import net.jeikobu.mediasorter.filters.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * MediaSorter - Created by shindouj on 2017-04-13
 */

public class MediaSorterMainClass {

    private static List<FileCopyTask> retryQueue = new ArrayList<>();
    private static Config             config;
    private static Logger             logger;
    private static XStream            xstream;
    private static boolean            scanLock;


    public static void main(String... args) {
        // Logger (log4j) init
        Properties props = System.getProperties();
        props.setProperty("log4j.configurationFile", "cfg/log4j2.xml");
        logger = LogManager.getLogger(MediaSorterMainClass.class);

        // XStream init
        try {
            xstream = new XStream(new StaxDriver());
            xstream.processAnnotations(AllTrueFilterGroup.class);
            xstream.processAnnotations(ExtensionFileFilter.class);
            xstream.processAnnotations(NameContainsFileFilter.class);
            xstream.processAnnotations(CaseInsensitiveNameContainsFileFilter.class);
            xstream.processAnnotations(OneOrMoreFilterGroup.class);
            xstream.processAnnotations(PrefixFileFilter.class);
            xstream.processAnnotations(CaseInsensitivePrefixFileFilter.class);
            xstream.processAnnotations(Category.class);
            xstream.processAnnotations(Categories.class);
            xstream.registerConverter(new PathConverter());
        } catch (Exception e) {
            logger.error(e);
            return;
        }

        logger.debug("XStream initialized successfully!");

        // Config (cfg4j) init
        try {
            ConfigFilesProvider configFilesProvider = () -> Arrays.asList(Paths.get("config.yaml"));
            ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
            Environment environment = new ImmutableEnvironment("./cfg");

            ConfigurationProvider cp = new ConfigurationProviderBuilder()
                    .withConfigurationSource(source)
                    .withEnvironment(environment)
                    .build();

            config = cp.bind("MSConfig", Config.class);
        } catch (Exception e) {
            logger.error(e);
            return;
        }

        logger.debug("cfg4j initialized successfully!");

        // Task scheduling
        ScheduledExecutorService ses;

        try {
            ses = Executors.newScheduledThreadPool(1);
        } catch (Exception e) {
            logger.error(e);
            return;
        }
        logger.debug("Task scheduled successfully!");

        Runnable scanning = new Runnable() {
            @Override
            public void run() {
                if (!scanLock) {
                    scanLock = true;
                    logger.debug("Retry queue: " + retryQueue.toString());

                    // Retry queue processing
                    ListIterator<FileCopyTask> retryQueueIterator = retryQueue.listIterator();
                    while(retryQueueIterator.hasNext()) {
                        FileCopyTask currentTask = retryQueueIterator.next();
                        if (currentTask.getCopyTrials() > config.copyRetries()) {
                            retryQueueIterator.remove();
                        } else {
                            try {
                                Files.copy(currentTask.getCopyFromPath(), currentTask.getCopyToPath());
                            } catch (IOException e) {
                                currentTask.incrementCopyTrials();
                                logger.error(e);
                            }
                        }
                    }

                    // Categories deserialization
                    Categories categories = new Categories();

                    try {
                        categories = (Categories) xstream.fromXML(new FileInputStream(config.filterPath().getAbsolutePath()));
                    } catch (FileNotFoundException e) {
                        logger.error(e);
                    }

                    logger.debug("Loaded categories: " + categories.getList().toString());

                    // Sorting files
                    File directory = config.listenPath();
                    for (File f: directory.listFiles()) {
                        List<FileCopyTask> filesToCopy = getListOfCopyTasks(categories, f);
                        if (filesToCopy.size() != 1) {
                            logger.error("File " + f + " fits to more than one category! Make your filters more precise.");
                        } else {
                            FileCopyTask currentTask = filesToCopy.get(0);
                            logger.debug("File " + currentTask.getCopyFromPath() + " belongs to " + currentTask.getCopyCategory().getLabel() + ".");
                            try {
                                Files.move(currentTask.getCopyFromPath(), currentTask.getCopyToPath());
                                logger.debug("Moved " + currentTask.getCopyFromPath() + " to " + currentTask.getCopyToPath() + " successfully.");
                            } catch (IOException e) {
                                logger.error(e);
                                if (currentTask.getCopyTrials() == 0 && config.retryEnabled()) {
                                    retryQueue.add(currentTask.incrementCopyTrials());
                                    logger.error("File has been added to retry queue.");
                                }
                            }
                        }
                    }
                    scanLock = false;
                } else {
                    logger.debug("Scanning locked by previous queue. Aborting.");
                }
            }
        };

        ses.scheduleAtFixedRate(scanning, 0, config.listenInterval(), TimeUnit.SECONDS);
    }

    private static List<FileCopyTask> getListOfCopyTasks(Categories cats, File f) {
        List<FileCopyTask> returnList = new ArrayList<>();
        for (Category c: cats.getList()) {
            try {
                returnList.add(new FileCopyTask(c.whichCategoryBelongsTo(f), f));
            } catch (MoreThanOneSubcategoryException e) {
                logger.error(e);
                break;
            } catch (DoesNotBelongToThisCategory e) {
                continue;
            }
        }
        return returnList;
    }

}
