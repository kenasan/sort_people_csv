package com.intexsoft.javacourse.tsymmerman;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.intexsoft.javacourse.tsymmerman.Constants.*;

/**
 * Scan directory, reed files people_*.csv, sort lines of documents for age and
 * sex and write sorted information in files in output directory.
 */
class FileUtils implements FileUtilsI {

    private List<String[]> maleLess40;
    private List<String[]> maleMore40;
    private List<String[]> femaleLess40;
    private List<String[]> femaleMore40;
    private String[] headLine;
    private CSVWriter csvWriter;
    private File file;

    /**
     * Constructor for initialization.
     */
    public FileUtils() {
        this.maleLess40 = new ArrayList<>();
        this.maleMore40 = new ArrayList<>();
        this.femaleMore40 = new ArrayList<>();
        this.femaleLess40 = new ArrayList<>();
    }

    /**
     * Method scan input directory and return array of *.csv file.
     *
     * @param inputDirectory - taking input directory from parameters args.
     * @return array of *.csv file.
     */
    public File[] readDirectory(String inputDirectory) {

        File inputFiles = new File(inputDirectory);
        return inputFiles.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
    }

    /**
     * Create and write sorted for age and sex information at files in empty output directory.
     *
     * @param outputDirectory - taking output directory from parameters args.
     */
    public void writeOutputFiles(String outputDirectory) {
        try {
            if (isClearOutputDirectory(outputDirectory))
                writeFiles(outputDirectory);
            else System.out.printf("Don't can save documents, clear output directory: %s.%n", outputDirectory);
        } catch (NullPointerException e) {
            System.out.printf("Incorrect output directory: %s %n", outputDirectory);
        }
    }

    /**
     * Scan array of *.csv file and take file contains "people" in name.
     * Reed file and sort it from criteria.
     *
     * @param filesCSV - array of *.csv file from input directory.
     */
    public void readFilesCSV(File[] filesCSV) {
        for (File file : filesCSV) {
            if (file.getName().contains(FILE_NAME_CONTAINS)) {
                this.file = file;
                List<String[]> fileLines = readFile(file);
                sort(fileLines);
                System.out.printf("File %s has been checked and reed %n", file.getName());
            } else System.out.printf("File: %s  - has wrong name, and not used.%n", file.getName());
        }
    }

    private boolean isClearOutputDirectory(String outputDirectory) throws NullPointerException {
        File file = new File(outputDirectory);
        File[] outputFiles = file.listFiles((dir, name) -> name.contains(FILE_NAME_FEMALE_LESS) ||
                name.contains(FILE_NAME_MALE_LESS) || name.contains(FILE_NAME_FEMALE_MORE) ||
                name.contains(FILE_NAME_MALE_MORE));
        return outputFiles.length == 0;
    }

    private void writeFiles(String outputDirectory) {
        writeSortedFile(maleLess40, outputDirectory, FILE_NAME_MALE_LESS);
        writeSortedFile(maleMore40, outputDirectory, FILE_NAME_MALE_MORE);
        writeSortedFile(femaleLess40, outputDirectory, FILE_NAME_FEMALE_LESS);
        writeSortedFile(femaleMore40, outputDirectory, FILE_NAME_FEMALE_MORE);
    }

    private List<String[]> readFile(File file) {
        List<String[]> strings = new LinkedList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            headLine = csvReader.readNext();
            strings = csvReader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    private void sort(List<String[]> stringsCsvFiles) {
        for (String[] line : stringsCsvFiles) {
            if (line[1].equals("male")) {
                sortByAge(line, maleLess40, maleMore40);
            } else if (line[1].equals("female")) {
                sortByAge(line, femaleLess40, femaleMore40);
            } else {
                System.out.printf("Don't have key word's in line: %s, of file : %s.%n", Arrays.toString(line), file.getName());
            }
        }
        stringsCsvFiles.clear();
    }

    private void sortByAge(String[] line, List<String[]> listLess40, List<String[]> listMore40) throws NumberFormatException {
        try {
            if (Integer.parseInt(line[2]) < 40) {
                listLess40.add(line);
            } else {
                listMore40.add(line);
            }
        } catch (NumberFormatException e) {
            System.out.printf("In line : %s , of file : %s - column 'age' have wrong number.%n", Arrays.toString(line), file.getName());
        }
    }

    private void writeSortedFile(List<String[]> list, String outputDirectory, String name) {
        int fileNumber = 1;
        int countLines = 0;
        if (list.size() > 0) {
            String fileName = getFileName(name, fileNumber);
            csvWriter = getCsvWriter(outputDirectory, fileName, csvWriter);
            System.out.printf("Create file: %s, at directory - %s %n", fileName, outputDirectory);
            csvWriter.writeNext(headLine);
            for (String[] line : list) {
                countLines++;
                if (countLines == 10) {
                    closeCsvWriter();
                    fileNumber++;
                    fileName = getFileName(name, fileNumber);
                    csvWriter = getCsvWriter(outputDirectory, fileName, csvWriter);
                    System.out.printf("Create file: %s, at directory - %s %n", fileName, outputDirectory);
                }
                csvWriter.writeNext(line);
            }
            closeCsvWriter();
        } else System.out.printf("Don't have a string with %s%n", name);
    }

    private void closeCsvWriter() {
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String name, int number) {
        return String.format("%s_%d", name, number);
    }

    private CSVWriter getCsvWriter(String outputDirectory, String fileName, CSVWriter csvWriter) {
        try {
            csvWriter = new CSVWriter(new FileWriter(outputDirectory + "/" + fileName + ".csv"));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return csvWriter;
    }
}
