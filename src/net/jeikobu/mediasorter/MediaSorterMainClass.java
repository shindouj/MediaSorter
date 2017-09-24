package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import net.jeikobu.mediasorter.datacontainers.CategorizedFile;
import net.jeikobu.mediasorter.datacontainers.ParsedFile;
import net.jeikobu.mediasorter.exceptions.*;
import net.jeikobu.mediasorter.filters.*;
import net.jeikobu.mediasorter.interfaces.Config;
import net.jeikobu.mediasorter.parsers.MovieParser;
import net.jeikobu.mediasorter.parsers.ParserHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.io.*;
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
        //props.setProperty("python.import.site", "false");
        logger = LogManager.getLogger(MediaSorterMainClass.class);

        // XStream init
        if (!xstreamInit(logger)) return;

        // Config (cfg4j) init
        if (!cfg4jInit(logger)) return;

        // Registering default parsers
        if (!defaultParserRegister(logger)) return;

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
                                currentTask.performMove();
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
                    } catch (Exception e) {
                        logger.error(e);
                    }

                    logger.debug("Loaded categories: " + categories.getList().toString());

                    // Categorizing files
                    File directory = config.listenPath();

                    List<CategorizedFile> categorizedFiles = new ArrayList<>();

                    if (directory.listFiles() != null) {
                        for (File f: directory.listFiles()) {
                            try {
                                Category cat = getFileCategory(categories, f);
                                if (cat == null) continue;
                                categorizedFiles.add(new CategorizedFile(f, cat));
                            } catch (MoreThanOneCategoryException e) {
                                logger.error(e);
                                continue;
                            } catch (MoreThanOneSubcategoryException e) {
                                logger.error(e);
                                continue;
                            }
                        }
                    }

                    logger.debug(categorizedFiles.toString());

                    // Sorting files

                    try {
                        for (CategorizedFile cf: categorizedFiles) {
                            FileCopyTask currentTask = new FileCopyTask(cf.getCategory(), cf.getFile());
                            logger.debug("File " + currentTask.getCopyFromPath()
                                    + " belongs to " + currentTask.getCopyCategory().getLabel() + ".");
                            try {
                                currentTask.performMove();
                                logger.debug("Moved " + currentTask.getCopyFromPath()
                                        + " to " + currentTask.getCopyToPath() + " successfully.");
                            } catch (IOException e) {
                                logger.error(e);
                                if (currentTask.getCopyTrials() == 0 && config.retryEnabled()) {
                                    retryQueue.add(currentTask.incrementCopyTrials());
                                    logger.error("File has been added to retry queue.");
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e);
                        e.printStackTrace();
                    }

                    // Parsing files
                    List<ParsedFile> parsedFiles = new ArrayList<>();
                    for (CategorizedFile cf: categorizedFiles) {
                        try {
                            logger.debug(cf.getCategory().getParser().fromCategorizedFile(cf).getMetadata().toString());
                        } catch (UnknownParserException e) {
                            logger.error(e);
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

    private static Category getFileCategory(Categories cats, File f)
            throws MoreThanOneCategoryException, MoreThanOneSubcategoryException {
        Category returnCat = null;
        for (Category c: cats.getList()) {
            try {
                Category currentCat = c.whichCategoryBelongsTo(f);
                if (currentCat != null && returnCat != null) {
                    throw new MoreThanOneCategoryException(f);
                } else if (currentCat != null) {
                    returnCat = currentCat;
                }
            } catch (DoesNotBelongToThisCategory e) {
                continue;
            }
        }
        return returnCat;
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

    private static boolean cfg4jInit(Logger logger) {
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
            return false;
        }

        logger.debug("cfg4j initialized successfully!");
        return true;
    }

    private static boolean xstreamInit(Logger logger) {
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
            return false;
        }

        logger.debug("XStream initialized successfully!");
        return true;
    }

    private static boolean defaultParserRegister(Logger logger) {
        try {
            ParserHandler.get().registerParser(new MovieParser());
            Thread.sleep(2000);
        } catch (ParserException e) {
            logger.error(e);
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("Default parsers registered successfully!");
        return true;
    }

}
