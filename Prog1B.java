import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Prog1B {

    private static int lengthOfRecord;

    // There are four fields that are integers each taking 4 bytes...4 * 4 = 16
    private static final int NUMBEROFBYTESFORINTEGERS = 16;

    private static final int NUMBEROFBYTESUSEDFORFIELDLENGTHS = 32;

    private static long numOfBytesInFile;

    private static int numOfRecords;

    private static ArrayList<Integer> stringFieldLengths;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You must provide a bin file as an CLI argument.");
            System.exit(1);
        }

        String filepath = args[0];
        boolean isBinFile = PersonalUtil.verifyExtensionIsCorrect(filepath, "bin");

        if (!isBinFile) {
            System.out.println("Please pass in a .bin file to run this program");
            System.exit(1);
        }
        concatenationLoop(openBinFile(filepath));
    }

    private static RandomAccessFile openBinFile(String filePath) {
        File file = null;
        RandomAccessFile binFile = null;
        try {
            file = new File(filePath);
            numOfBytesInFile = Files.size(Paths.get(filePath)) - NUMBEROFBYTESUSEDFORFIELDLENGTHS;
            binFile = new RandomAccessFile(file, "rw");
        } catch (Exception e) {
            System.out.println("An error occured while opening the bin file");
            System.exit(-1);
        }

        return binFile;
    }

    private static void concatenationLoop(RandomAccessFile binFile) {
        Scanner scanner = new Scanner(System.in);
        stringFieldLengths = CSVRecord.getStringFieldLengths(binFile, numOfBytesInFile );

        // Add the number of bytes each of the strings take and each of the iteger fields
        lengthOfRecord = stringFieldLengths.stream().mapToInt(Integer::intValue).sum();
        lengthOfRecord += NUMBEROFBYTESFORINTEGERS;

        // The number of records = ( Number of bytes in a file minus 32 bytes for field lengths ) / length of single record.
        numOfRecords = (int) (numOfBytesInFile / lengthOfRecord);

        // Do standard print for what the spec asks for
        standardRecordPrint(binFile);

        while (true) {
            System.out.println("\nPlease enter a concatenation");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("e")) {
                break;
            }

            searchTheRecords(binFile, input, 0, numOfRecords - 1);
        }
        
        scanner.close();
    }

    /**
     * print to the
       screen the content of the ‘State FIPS’, ‘118th Congressional District’, ‘2017 NAICS Code’, ‘Employment’,
       and ‘Employment Noise Flag’ fields. fields of the first four records of data, the middle four records (or
       middle three records, if the quantity of records is odd), and the last four records of data. Next, display
       the total number of records in the binary file, on a new line. Conclude the output with a list, in ascending
       order by employment, of the ten states having the lowest total employments (display the state names
       and the their employment totals).
     * @param binFile
     */
    private static void standardRecordPrint( RandomAccessFile binFile ) {
        
        System.out.println("\nStandard Printing Required");
        System.out.println("---------------------------\n");

        // Only print the records we have if we have 4 or less records
        if ( numOfRecords <= 4 ) {
            for (int i = 0; i < numOfRecords; i++ ) {
                CSVRecord record = CSVRecord.fetchObject(binFile, stringFieldLengths, i * lengthOfRecord );
                record.printRecordPartB();   
            }

            return;
        }

        // Print the first four records in the file
        for ( int i = 0; i < 4; i++ ) {
            CSVRecord record = CSVRecord.fetchObject(binFile, stringFieldLengths, i * lengthOfRecord );
            record.printRecordPartB();
        }
        

        // Print the middle records 
        int middleStart;
        int middleEnd;
        boolean evenNumberOfRecords = numOfRecords % 2 == 0;

        if ( evenNumberOfRecords ) {
            middleStart = ( numOfRecords / 2 ) - 1;
            middleEnd = middleStart + 3;
            while ( middleStart <= middleEnd ) {
                CSVRecord record = CSVRecord.fetchObject(binFile, stringFieldLengths, middleStart * lengthOfRecord );
                record.printRecordPartB();
                middleStart++;
            }
            middleStart++;
        } else {
            middleStart = ( numOfRecords / 2 );
            middleEnd = middleStart + 2;
            while ( middleStart <= middleEnd ) {
                CSVRecord record = CSVRecord.fetchObject(binFile, stringFieldLengths, middleStart * lengthOfRecord );
                record.printRecordPartB();
                middleStart++;
            }
        }

        // Print the last four records
        int endStart = numOfRecords - 4;

        while (endStart < numOfRecords) {
            CSVRecord record = CSVRecord.fetchObject(binFile, stringFieldLengths, endStart * lengthOfRecord);
            record.printRecordPartB();
            endStart++;
        }

        System.out.println("\nSome Extra Information\n----------------------");
        System.out.println("The total number of records is: " + numOfRecords );
    }

    private static int searchTheRecords(RandomAccessFile binFile, String toSearchFor, int left, int right ) {
        if ( left > right ) {
            // The record was not found
            return -1;
        }

        int oneThird = ((left + (right - 1 )) / 3) + NUMBEROFBYTESUSEDFORFIELDLENGTHS;
        int twoThird = ((right - (right - 1)) / 3) + NUMBEROFBYTESUSEDFORFIELDLENGTHS;

        CSVRecord recordOne = CSVRecord.fetchObject(binFile, stringFieldLengths, oneThird);
        CSVRecord recordTwo = CSVRecord.fetchObject(binFile, stringFieldLengths, twoThird);

        


        return -1;
    }


}
