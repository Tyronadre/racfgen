package net.henrik.utils;

import java.io.*;

public class RACFWriter extends BufferedWriter {
    public RACFWriter(File file) throws IOException {
        super(new FileWriter(file, true));
    }

    public void writeMyLine(String id, String racf, String name) throws IOException {
        write(id + "•" + racf + "•" + name + "\n");
    }
}
