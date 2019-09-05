package com.intexsoft.javacourse.tsymmerman;

import java.io.File;

public interface FileUtilsI {

    File[] readDirectory(String inputDirectory);

    void writeOutputFiles(String outputDirectory);

    void readFilesCSV(File[] filesCSV);
}
