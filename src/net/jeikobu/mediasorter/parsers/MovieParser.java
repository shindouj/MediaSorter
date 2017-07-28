package net.jeikobu.mediasorter.parsers;

import com.google.gson.Gson;
import net.jeikobu.mediasorter.datacontainers.CategorizedFile;
import net.jeikobu.mediasorter.datacontainers.ParsedVideo;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.File;

/**
 * MediaSorter - Created by shindouj on 16/04/2017
 */
public class MovieParser implements Parser {
    private static final String parserName = "MovieParser";

    public String getName() {
        return parserName;
    }

    private String runPython(String fileName) {
        PythonInterpreter pyInterpreter = new PythonInterpreter();
        PySystemState sys = Py.getSystemState();
        sys.path.append(new PyString("resources/PTN"));
        pyInterpreter.set("fileName", fileName);
        pyInterpreter.execfile("resources/PTN/runparse.py");
        PyObject answer = pyInterpreter.eval("repr(parse(fileName))");
        return answer.toString().substring(1, answer.toString().length() - 1);
    }

    @Override
    public ParsedVideo fromCategorizedFile(CategorizedFile cf) {
        String jsonMetadata = runPython(cf.getFile().getName());
        Gson gson = new Gson();
        MovieParserResponse mpr = gson.fromJson(jsonMetadata, MovieParserResponse.class);
        return mpr.toParsedVideo(cf);
    }

    @Override
    public String getFormat() {
        return null;
    }
}
