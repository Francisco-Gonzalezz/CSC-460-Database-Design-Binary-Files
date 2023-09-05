/*
 * Author: Francisco Gonzalez
 * Course: CSC 460 ( Database Design )
 * Assignment: Program 1 Part A
 * Instructor: Prof. McCann
 * TAs: Zhenyu Qi, Danial Bazmandeh
 * Due Date: August 31, 2023
 * 
 * This programs purpose is transfer the data from a csv file into a bin file 
 * in order to be able to find records more efficiently. In order to run the program 
 * the user must pass in a csv file as the first command line argument. The file
 * extension is verified to be a .csv file. The program then takes a look through
 * the file in order to grab the longest string lengths of the string fields, this
 * information is then stored into an integer array so that we can write the lengths
 * into the bin file when we begin writing to it. The program then opens the CSV file 
 * at the beginning again and reads the file one line at a time creating CSVRecord objects
 * with the 12 fields stored within that object, as these objects are created they are stored 
 * within an ArrayList. After the csv file is done being read the ArrayList is then sorted
 * using the List.sort() method ( This sorting algorithm utilizes Timsort which in 
 * worst case runs in O(NlogN) ) using a custom comparator that compares each of the
 * CSVRecord obbjects based on the concatenation of their State FIPS code, 118th Congressional District,
 * , and their 2018 NAICS Code. After this sorting is done we then delete any previous bin files
 * that were created with this program and csv combo. The program then creates a new bin file
 * using the original filename + .bin extension using the RandomAcccessFile object.
 * The program then iterates through the ArrayList of CSVObjects dumping their information
 * into the bin file after this is done the string field lengths are then written to the end 
 * of the bin file. 
 * 
 * Fields ( static )
 * --------------------------
 * - CSVFILEEXTENSION (String) - This field is the file extension that the program is expecting
 *                                  to read from.
 * 
 * Fields ( non-static )
 * ------------------------
 * 
 * - fileName (String) - The file name that was extracted from the file path
 * - longestLengthState (Integer) - Length of the longest state name in CSV file
 * - longestDescription (Integer) - Length of longest NAICS Description
 * - longestDistrict (Integer) - Length of the longest Congressional District
 * - longestNAICSCode (Integer) - Length of longest NAICS Code
 * - longestEmploymentFlag (Integer) - Length of longest employment flag
 * - longestFirstQuarterNoiseFlag (Integer) - Length of longest first quarter noise flag
 * - longestStateFIPS (Integer) - Length of longest state FIPS
 * - fieldLengths (Integer Array) - Array storing the lengths of the above longestX fields.
 *  
 * 
 *  Java Version: 16 
 *  
 *  Command Line Arguments
 *  ------------------------
 *  This program only accepts a single command line argument which must be the filepath to the csv file that you want
 *  to write to a bin file.
 * 
 *  In order to run this program follow these steps
 * ---------------------------------------------------
 *  1) Compile the program running javac Prog1A.java ( This should compile CSVRecord.java and FileUtils.java at the same time since there is a dependency there )
 *  2) Run 'java Prog1A <csv file>'
 *  3) A binfile output will be produced in the directory of this program.
 * 
 */
import java.io.*;
import java.util.*;

public class Prog1A {

    private final static String CSVFILEEXTENSION = "csv";

    private static String fileName; 
    private static int longestLengthState;
    private static int longestDescription;
    private static int longestDistrict;
    private static int longestNAICSCode;
    private static int longestEmploymentFlag;
    private static int longestFirstQuraterNoiseFlag;
    private static int longestAnnualNoiseFlag;
    private static int longestStateFIPS;
    private static int[] fieldLengths;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a csv file to read");
            System.exit(1);
        }
        String filePath = args[0];
        boolean isACSVFile = FileUtils.verifyExtensionIsCorrect(filePath, CSVFILEEXTENSION );

        if (!isACSVFile) {
            System.out.println("Please provide a .csv file");
            System.exit(1);
        }
        preprocessDataForLongestStrings(filePath);

        readCSV(filePath);
    }

    /**
     * Deletes fileName.bin if it exists before creating a new file with the same name.
     * After creating the file the function then iterates through the List param that was given
     * writing each of CSVRecord objects into the bin file. After this is done then
     * the function will then write all the string field lengths to the end of the 
     * file in order to be able to read contents of the bin file correctly.
     * @param records
     */
    private static void writeBinaryFile( List<CSVRecord> records ) {
        File fileRef = null;
        RandomAccessFile binFile = null;

        // If an old version of this binary file exists, delete it.
        // We can overwrite an old file, but it's safer to delete
        // and start fresh
        try {
            fileRef = new File(fileName + ".bin");
            if (fileRef.exists()) {
                fileRef.delete();
            }
        } catch (Exception e) {
            System.out.println("I/O ERROR: Something went wrong with the " + "deletion of the previous binary file.");
            System.exit(-1);
        }


        // (Re)create the binary file
        try {
            binFile = new RandomAccessFile(fileRef, "rw");

        } catch (FileNotFoundException e) {
            System.out.println("I/O ERROR: Something went wrong with the "
                    + "creation of the RandomAccessFile object.");
            System.exit(-1);
        }
        
        // Dump each of the CSVRecord objects into the binfile
        for ( CSVRecord record : records ) {
            record.dumpObject(binFile, fieldLengths);
        }

        // Add the field lengths at the very end of the bin file
        for ( Integer number : fieldLengths ) {
            try {
                binFile.writeInt(number);
            } catch ( IOException e ){
                System.out.println("There was an error writing the field lengths to the bin file");
            }
        }


    }

    /**
     * Takes an inital pass through the csv file to find the longest string lengths in 
     * each of the string columns which are 
     * 
     * 1) State FIPS ( String )
     * 2) State Name ( String )
     * 3) 118th Congressional District ( String )
     * 4) 2017 NAICS Code ( String )
     * 5) NAICS Description ( String )
     * 6) Employment Noise Flag ( String )
     * 7) 1st Quarter Payroll Noise Flag ( String )
     * 8) Annual Payroll Noise Flag ( String )
     * 
     * Stores the longest string field lengths into the int array fieldLengths 
     * @param filepath
     */
    private static void preprocessDataForLongestStrings(String filepath) {
        File file = null;
        BufferedReader reader = null;

        try {
            file = new File(filepath);
            reader = new BufferedReader(new FileReader(file));

            // Read the first line to skip over the metadata
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Gather the 8 string fields
                String fips = data[0];
                String state = data[1];
                String district = data[2];
                String naicsCode = data[3];
                String naicsDescription = data[4];
                String employmentNoiseFlag = data[7];
                String firstQuarterNoiseFlag = data[9];
                String annualNoiseFlag = data[11];

                longestStateFIPS = Math.max(fips.length(), longestStateFIPS);
                longestLengthState = Math.max(state.length(), longestLengthState);
                longestDistrict = Math.max(district.length(), longestDistrict);
                longestNAICSCode = Math.max(naicsCode.length(), longestNAICSCode);
                longestDescription = Math.max(naicsDescription.length(), longestDescription);
                longestEmploymentFlag = Math.max(employmentNoiseFlag.length(), longestEmploymentFlag);
                longestFirstQuraterNoiseFlag = Math.max(firstQuarterNoiseFlag.length(), longestFirstQuraterNoiseFlag);
                longestAnnualNoiseFlag = Math.max(annualNoiseFlag.length(), longestAnnualNoiseFlag);
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("I/O ERROR: Couldn't open, or couldn't read "
                    + "from, the CSV file.");
            System.exit(-1);
        }

        fieldLengths = new int[] { longestStateFIPS, longestLengthState, longestDistrict, longestNAICSCode,
                longestDescription,
                longestEmploymentFlag, longestFirstQuraterNoiseFlag, longestAnnualNoiseFlag };

    }

    /**
     * Read the CSV file that is passed in and create CSVRecord objects to store
     * information on each line.
     * Each line is read in the following order
     * 1) State FIPS ( String )
     * 2) State Name ( String )
     * 3) 118th Congressional District ( String )
     * 4) 2017 NAICS Code ( String )
     * 5) NAICS Description ( String )
     * 6) Number of Establishments ( Integer )
     * 7) Employment ( Integer )
     * 8) Employment Noise Flag ( String )
     * 9) 1st Quarter Payroll ( In $1000 ) ( Integer )
     * 10) 1st Quarter Payroll Noise Flag ( String )
     * 11) Annual Payroll ( In $1000 ) ( Integer )
     * 12) Annual Payroll Noise Flag ( String )
     *
     * This function stores the objects into an ArrayList to dump to a bin file 
     * after all of the objects are created.
     * @param filePath: String containing the path to the csv file to read
     * @return An ArrayList of CSVRecord created from the CSV File.
     */
    private static void readCSV(String filePath) {

        File file = null;
        BufferedReader reader = null;
        ArrayList<CSVRecord> records = new ArrayList<>();

        try {
            file = new File(filePath);
            reader = new BufferedReader(new FileReader(file));

            // Read the line out of the beginning so we can start looking at the data in the
            // loop
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                CSVRecord record = new CSVRecord();
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Set the field variable in the CSVRecord Object we created.
                record.setStateFIPS(data[0]);
                record.setState(data[1]);
                record.setCongressionalDistrict(data[2]);
                record.setCodeNAICS(data[3].replace("-", ""));
                record.setNaicsDescription(data[4].replace("\"", ""));
                record.setNumOfEstablishments(Integer.parseInt(data[5]));
                record.setEmployment(Integer.parseInt(data[6]));
                record.setEmploymentNoiseFlag(data[7]);
                record.setFirstQuarterPayroll(Integer.parseInt(data[8]));
                record.setFirstQuarterPayrollNoiseFlag(data[9]);
                record.setAnnualPayroll(Integer.parseInt(data[10]));
                record.setAnnualPayrollNoiseFlag(data[11]);

                records.add(record);
            }

            // Close the reader to free system resources
            reader.close();
        } catch (IOException e) {
            System.out.println("I/O ERROR: Couldn't open, or couldn't read "
                    + "from, the CSV file.");
            System.exit(-1);
        }

        sortRecords(records);
        writeBinaryFile(records);
    }

    /**
     * Sorts a List<CSVRecord> based on their concatenation of the State FIPS, 118th Congressional district,
     * and 2018 NAICS Code. This function utilizes the List.sort() method which uses a version
     * of Timsort meaning that in worst case it will finish sorting in NlogN time.
     * @param records
     */
    private static void sortRecords( List<CSVRecord> records ) {
        Comparator<CSVRecord> comparator = new Comparator<CSVRecord>() {
            @Override
            public int compare(CSVRecord o1, CSVRecord o2) {
                String firstConcat = o1.getStateFIPS() + o1.getCongressionalDistrict() + o1.getCodeNAICS();
                String secondConcat = o2.getStateFIPS() + o2.getCongressionalDistrict() + o2.getCodeNAICS();

                return firstConcat.compareTo(secondConcat);
            }
        };

        records.sort(comparator);
    }
}