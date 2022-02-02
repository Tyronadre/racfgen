package net.henrik.utils;

import java.io.*;

public class RACFReader extends BufferedReader {
    public RACFReader(File file) throws FileNotFoundException {
        super(new FileReader(file));
    }


    public String[] readMyLine() throws IOException {
        String temp = readLine();
        if (temp == null)
            return null;
        return temp.split("•");
    }


}
