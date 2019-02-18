package me.lotabout.springsecurityexample.common.util;

import java.io.*;
import java.util.stream.Collectors;

public class ConfigLoader {

    public static String loadConf(String path) throws FileNotFoundException {
        final BufferedReader reader;
        if (path.startsWith("classpath:")) {
            String parsedPath = path.replace("classpath:", "");
            InputStream is = ConfigLoader.class.getResourceAsStream(parsedPath);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
        } else {
            reader = new BufferedReader(new FileReader(new File(path)));
        }

        return reader.lines().collect(Collectors.joining("\n"));
    }
}

