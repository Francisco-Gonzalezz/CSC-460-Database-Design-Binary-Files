/**
 * Author: Francisco Gonzalez
 * Course: CSC 460 ( Database Design )
 * Assignment: Program 1 Part B
 * Intructor: Prof. McCann
 * TAs: Zhenyu Qi, Danial Bazmandeh
 * Due Date: September 7th, 2023
 * 
 * This program's purpose is to read from a bin file allowing the program to efficiently search for records. In order to run the 
 * program the user must pass in a bin file that was generated from Part A of this assignment. The file extension is verified to be a
 * .bin file. The program reads the last 32 bytes which store the string field lengths for a CSVRecord object. It then prints the first
 * four records within the bin file the middle records and the last four records. This is followed by printing the amount of records
 * that exist within the bin file and a list of ten states in ascending order from their employment. After this is completed then 
 * the program will enter a loop where it asks the user for input so that they can search for records themselves. If the record can be 
 * located ( Using Terenary Search ) then it will print as specified within the Part B spec. If the record cannot be located then a message
 * will be displayed to the user and they will continue to search for more records. If the user wants to exit the program they can type
 * E ( Case-insensitve ). 
 * 
 * Fields ( Final )
 * - NUMBEROFBYTESFORINTEGERS ( Integer ) - This is the amount of bytes that the integer fields take within the binFile which is 16 bytes
 * 
 * - NUMBEROFBYTESUSEDFORFIELDLENGTHS ( Integer ) - There are 8 integer values within the bin file that are used for metadata meaning ( 8 fields * 4 bytes each  = 32 bytes )
 * 
 * - BEGINNINGOFFILE ( Integer ) - The location within the bin file that is the beginning of the records
 * 
 * 
 * Fields ( Non-Final )
 * - numOfByesInFile ( long ) - Number of bytes within the bin file excluding ones used for metadata
 * 
 * - numOfRecords ( Integer ) - The number of records that are within the bin file
 * 
 * - stringFieldLengths ( List<Integer> ) - The 8 different lengths for string fields.
 * 
 * Java Version: 16 
 * 
 * Command Line Arguments
 * -----------------------
 * This program only accepts a single command line argument which must be a filepath to the bin file that is to be read and 
 * searched through.
 * 
 * In order to run this program follow these steps
 * -----------------------------------------------
 * 1) Compile the program running 'javac Prog1B.java' ( This should compile CSVRecord.java and FileUtils.java since this program is dependent upon them )
 * 2) run 'java Prog1B <bin file>'
 * 3) Enter concatenations of records in order to locate them within the bin file.
 */
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Prog1B {

    private static int lengthOfRecord;

    // There are four fields that are integers each taking 4 bytes...4 * 4 = 16
    private static final int NUMBEROFBYTESFORINTEGERS = 16;

    // There are 8 String fields whose lengths are store at the end of the bin file meaning they take 32 bytes within the bin file
    private static final int NUMBEROFBYTESUSEDFORFIELDLENGTHS = 32;

    // Start index of the bin file
    private static final int BEGINNINGOFFILE = 0;

    // The amount of bytes within the bin file excluding the bytes for metadata
    private static long numOfBytesInFile;

    // The amount of records that are found within the bin file
    private static int numOfRecords;

    // The 8 string field lengths that will be used to read from the bin file
    private static ArrayList<Integer> stringFieldLengths;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You must provide a bin file as an CLI argument.");
            System.exit(1);
        }

        String filepath = args[0];

        // Verify that the file is a .bin file 
        boolean isBinFile = FileUtils.verifyExtensionIsCorrect(filepath, "bin");

        if (!isBinFile) {
            System.out.println("Please pass in a .bin file to run this program");
            System.exit(1);
        }

        concatenationLoop(openBinFile(filepath));
    }


    /**
     * Using a filePath determine the amount of bytes inside the binFile by reading the amount of bytes in the file
     * minus the amount of bytes that it takes to store the 8 string field lengths (32 bytes). Then opens the binFile
     * specified by the filePath and return that binFile to be used within searching and printing.
     * @param filePath
     * @return RandomAccessFile binFile at the location specified with CLI Argument.
     */
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

    /**
     * Main loop that will continue to ask for a concatenation from the user until the user enters the letter E case insesnsitive.
     * Will read the binFile to get the length of all the string field lengths, in order to properly read from the binFile.
     * 
     * Determines the length of a record by adding the amount of bytes that it took for each string field length and the amount
     * of bytes that were required for the integer fields.
     * 
     * The number of records within the file is calculated by dividing the amount of bytes within the file by
     * the length of each record.
     * 
     * If the number of records is 0 or less ( I don't know how a negative number would be acheived but catch case here )
     * the program will print a message to the user and exit the program with no error.
     * 
     * Will then do the prints required by the spec by calling the standardRecordPrint( RandomAccessFile binFile ) function
     * 
     * 
     * Searches the records to find the concatenation that the user has entered and if the record could not be found
     * then a message will be printed out and otherwise the standard print out will be done. 
     * 
     * @param binFile
     */
    private static void concatenationLoop(RandomAccessFile binFile) {
        Scanner scanner = new Scanner(System.in);
        stringFieldLengths = CSVRecord.getStringFieldLengths(binFile, numOfBytesInFile );

        // Add the number of bytes each of the strings take and each of the iteger fields
        lengthOfRecord = stringFieldLengths.stream().mapToInt(Integer::intValue).sum();
        lengthOfRecord += NUMBEROFBYTESFORINTEGERS;

        // The number of records = ( Number of bytes in a file minus 32 bytes for field lengths ) / length of single record.
        numOfRecords = (int) (numOfBytesInFile / lengthOfRecord);

        if ( numOfRecords <= 0 ) {
            System.out.println("There are no records within this file. There is no point searching it");
            System.exit(0);
        }

        // Do standard print for what the spec asks for
        standardRecordPrint(binFile);

        while (true) {
            System.out.println("\nPlease enter a concatenation");
            String input = scanner.nextLine();
            System.out.println();

            // This will exit the program when the user sees fit
            if (input.equalsIgnoreCase("e")) {
                System.out.println("Thank you for exiting this program responsibly");
                break;
            }
           
            // Search the records using terenary search ( Note: Must trim the input to ensure there are no null characters that will mess up checking equality )
            int statusCode = searchTheRecords(binFile, input.trim(), BEGINNINGOFFILE, numOfRecords - 1 );

            // Print message if the record cannot be found
            if ( statusCode < 0 ) {
                System.out.println("Unable to find the record you listed.");
            }
        }
        
        // Close scanner to free some resources
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
            printExtraInfo(binFile);
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

        printExtraInfo(binFile);
    }

    /**
     * This function prints the number of Records and creates a HashMap that contains all the states within the 
     * bin file and their lowest number of employment.
     * 
     * After this hashmap is generated it will be passed into the printTenLowesetStates( Map<String, Integer> map ) function
     * in order to extact the lowest employment states
     */
    private static void printExtraInfo( RandomAccessFile binFile ) {
        System.out.println("\nSome Extra Information\n----------------------");
        System.out.println("The total number of records is: " + numOfRecords );

        getLowestEmployment(binFile);
    }

    private static void getLowestEmployment( RandomAccessFile binFile ) {
        Map<String, Integer> employmentMap = new HashMap<>();

        int recordNumber = 0;

        while ( recordNumber < numOfRecords ) {
            int byteIndex = recordNumber * lengthOfRecord;
            CSVRecord record = CSVRecord.fetchObject(binFile, stringFieldLengths, byteIndex);
            int employment = record.getEmployment();

            // If map contains this state already check to see if we found a lower employment number
            if ( employmentMap.containsKey( record.getState()) ) {
                int currentValue = employmentMap.get( record.getState() );
                if ( employment < currentValue ) {
                    employmentMap.put( record.getState(), employment );
                }
            } else {
                // This is the first time we have seen this state so place it into the map
                employmentMap.put( record.getState(), employment );
            }

            recordNumber++;
        }

        // Now that we have a map with lowest employment of each state we can find the 10 states with the lowest employment.
        printTenLowestStates(employmentMap);
    }

    /**
     * Using Java streams this function will search through the Map that was passed in and find the lowest Entry within the map
     * based off the value of the key. After this entry is found the key is removed from the Map and the the loop will start over.
     * This will loop either 10 times if there are at least 10 states within the map or however many states that are contained within 
     * the map.
     * 
     * Will build a string through each loop that will be printed out in the format of a list
     * @param employmentMap
     */
    private static void printTenLowestStates( Map<String, Integer> employmentMap ) {
        int i = 0;
        int maxIndex = numOfRecords < 10 ? numOfRecords : 10;
        StringBuilder sb = new StringBuilder("\nAscending List of Employment (Lowest Ten States):\n");

        // Add the states with the lowest employment in order from least to most
        while ( i < maxIndex && i < employmentMap.entrySet().size() ) {
            Entry<String, Integer> smallestEntry = employmentMap.entrySet().stream().min(Comparator.comparingInt(Entry::getValue)).get();
            sb.append((i + 1) + ") " + smallestEntry.getKey() + ": " + smallestEntry.getValue() + "\n");
            employmentMap.remove(smallestEntry.getKey());
            i++;
        }

        System.out.println(sb.toString());
    }

    /**
     * Will perform a terenary search through the binFile searching for the specified string. Terenary search is done
     * by doing a binary search but rather than checking the half way mark the oneThird and twoThird marks are checked.
     * The same logic follows if the search needs to go right or left within the binFile as it does using binary search.
     * 
     * @return Will return 0 if the record is able to be located and -1 otherwise.
     * @param binFile
     * @param toSearchFor
     * @param leftIndex
     * @param rightIndex
     */
    private static int searchTheRecords(RandomAccessFile binFile, String toSearchFor, int left, int right ) {
        
        // Left shouldn't be greater than right unless we were unable to locate the record.
       if ( left > right ) {
            return -1;
       }
       
       // Grab the oneThird and twoThird index within the binFile
       int oneThirdIndex = left + ( right - left ) / 3;
       int twoThirdIndex = left + ( 2 * ( right - left )) / 3;

       // Retrieve the records from the above indexes.
       CSVRecord recordOne = CSVRecord.fetchObject(binFile, stringFieldLengths, oneThirdIndex * lengthOfRecord );
       CSVRecord recordTwo = CSVRecord.fetchObject(binFile, stringFieldLengths, twoThirdIndex * lengthOfRecord );
       
       // Grab the concatenation strings from each of the CSVRecord objects and trim them to ensure no issues occur with equality
       String concatOne = recordOne.grabConcatentation().trim();
       String concatTwo = recordTwo.grabConcatentation().trim();

       // Check to see if any of the above records are the one that is being searched for
       if ( concatOne.equals(toSearchFor)) {
        recordOne.printRecordPartB();
        return 0;
       } else if ( concatTwo.equals(toSearchFor)) {
        recordTwo.printRecordPartB();
        return 0;
       }

       // Move left or right depending on the concatenation value that is being searched for.
       if ( toSearchFor.compareTo(concatOne) > 0 ) {
        left = oneThirdIndex + 1;
       } else if ( toSearchFor.compareTo(concatTwo) < 0 ) {
        right = twoThirdIndex - 1;
       }

       // Recurse to continue searching for the desired record.
        return searchTheRecords(binFile, toSearchFor, left, right);
    }


}
