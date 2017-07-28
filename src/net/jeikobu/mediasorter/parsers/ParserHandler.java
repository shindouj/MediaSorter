package net.jeikobu.mediasorter.parsers;

import net.jeikobu.mediasorter.exceptions.ParserException;
import net.jeikobu.mediasorter.exceptions.UnknownParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * MediaSorter - Created by shindouj on 02/05/2017
 */
public class ParserHandler {
    private static ParserHandler instance;
    private Logger logger;
    private Set<Parser> registeredParsers;

    private ParserHandler() {
        logger = LogManager.getLogger(ParserHandler.class);
        registeredParsers = Collections.synchronizedSet(new HashSet<>());
    }

    public static ParserHandler get() {
        if (instance == null) instance = new ParserHandler();
        return instance;
    }

    public void registerParser(Parser p) throws ParserException {
        synchronized (registeredParsers) {
            if (!registeredParsers.add(p)) {
                throw new ParserException("Parser " + p.getName() + " is already registered!");
            }
        }
    }

    public void deregisterParser(Parser p) throws UnknownParserException {
        synchronized (registeredParsers) {
            if (registeredParsers.remove(p)) {
                throw new UnknownParserException("Parser " + p.getName() + " was never registered!");
            }
        }
    }

    public Parser getParser(String parserName) throws UnknownParserException {
        synchronized (registeredParsers) {
            for (Parser p: registeredParsers) {
                System.out.println(p.getName() + " " + parserName);
                if (p.getName().equals(parserName)) return p;
            }
            throw new UnknownParserException("Parser " + parserName + " not found! Try registering it first, maybe?");
        }
    }


}
