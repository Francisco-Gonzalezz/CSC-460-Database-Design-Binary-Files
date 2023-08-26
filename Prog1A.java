import java.io.*;
import java.util.Map;

public class Prog1A {

    private final static int SUCCESSFULFILEEXTENSIONSPLIT = 2;
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
    private static boolean alreadyDeleted = false;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a csv file to read");
            System.exit(1);
        }
        String filePath = args[0];
        boolean isACSVFile = verifyFileIsCSV(filePath);

        if (!isACSVFile) {
            System.out.println("Please provide a .csv file");
            System.exit(1);
        }
        preprocessDataForLongestStrings(filePath);
        readCSV(filePath);
    }

    private static void writeBinaryFile(CSVRecord record) {
        File fileRef = null;
        RandomAccessFile binFile = null;

        // If an old version of this binary file exists, delete it.
        // We can overwrite an old file, but it's safer to delete
        // and start fresh
        try {
            fileRef = new File(fileName + ".bin");
            if (fileRef.exists() && !alreadyDeleted) {
                fileRef.delete();
            }
        } catch (Exception e) {
            System.out.println("I/O ERROR: Something went wrong with the " + "deletion of the previous binary file.");
            System.exit(-1);
        }
        alreadyDeleted = true;

        // (Re)create the binary file
        try {
            binFile = new RandomAccessFile(fileRef, "rw");

        } catch (FileNotFoundException e) {
            System.out.println("I/O ERROR: Something went wrong with the "
                    + "creation of the RandomAccessFile object.");
            System.exit(-1);
        }

        record.dumpObject(binFile, fieldLengths);

    }

    private static boolean verifyFileIsCSV(String filepath) {
        String[] filePathSplit = filepath.split("/");
        String[] fileNameAndExtension = filePathSplit[filePathSplit.length - 1].split("\\.");
        if (fileNameAndExtension.length < SUCCESSFULFILEEXTENSIONSPLIT) {
            System.out.println("This file has no extension, ensure you are passing in a csv file");
            System.exit(1);
        }

        fileName = fileNameAndExtension[0];
        String extension = fileNameAndExtension[1];

        return extension.equals(CSVFILEEXTENSION);
    }

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
     * After creating the object the function will call writeBinary() function to
     * write the current object
     * to the binary file.
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
     * @param filePath: String containing the path to the csv file to read
     * @return An ArrayList of CSVRecord created from the CSV File.
     */
    private static void readCSV(String filePath) {

        File file = null;
        BufferedReader reader = null;

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

                writeBinaryFile(record);
            }

            // Close the reader to free system resources
            reader.close();
        } catch (IOException e) {
            System.out.println("I/O ERROR: Couldn't open, or couldn't read "
                    + "from, the CSV file.");
            System.exit(-1);
        }
    }
}


