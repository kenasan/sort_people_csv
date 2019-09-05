package com.intexsoft.javacourse.tsymmerman;

import java.io.File;

import static com.intexsoft.javacourse.tsymmerman.Constants.FILE_NAME_CONTAINS;

/**
 * Main class of program.
 * Take input and output directory from input parameters args.
 * Reed strings from files people_*.csv in input directory and after save
 * files sorted by age and sex column in output directory.
 */
public class SortFileCsvIO {

    private static String inputDirectory;
    private static String outputDirectory;

    /**
     * Start point of program.
     * @param args - must have input and output directory when start.
     */
    public static void main(String[] args) {
        getIODirectory(args);
        FileUtils fileUtils = new FileUtils();
        File[] filesCsv = fileUtils.readDirectory(inputDirectory);

        if (isRightInputDirectory(filesCsv)) {
            fileUtils.readFilesCSV(filesCsv);
            fileUtils.writeOutputFiles(outputDirectory);
        }
    }

    private static boolean isRightInputDirectory(File[] filesCsv) {
        if (filesCsv == null) {
            System.out.printf("Incorrect input directory: %s %n", inputDirectory);
            return false;
        } else if (filesCsv.length == 0) {
            System.out.println("Don't have any file *.csv ");
            return false;
        } else {
            return true;
        }
    }

    private static void getIODirectory(String[] args) {
        try {
            inputDirectory = args[0];
            outputDirectory = args[1];
        } catch (NullPointerException e) {
            System.out.printf("Write input directory witch contains %s_*.csv, and output empty directory", FILE_NAME_CONTAINS);
        }
    }
}