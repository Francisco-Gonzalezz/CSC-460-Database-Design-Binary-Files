/**
 * Class: CSVRecord 
 * 
 * Author: Francisco Gonzalez
 * 
 * Purpose: An Object of this class holds the field values of one record of data in the format
 *          specified within the Program 1 assignment, meaning that there is one for each of the
 *          12 fields is stored within this object. The 12 fields that this object contains is
 *          State Name, NAICS Description, Employment Noise Flag, Annual Payroll Noise Flag, State FIPS
 *          Code, Congressional District, NAICS Code, Number of Establishments, Employment, First Quarter
 *          Payroll, and Annual Payroll.
 * 
 * Inherits From: None.
 * 
 * Interfaces: None
 * 
 * Constants:
 *          - BEGINNINGOFFILE -- The starting byte of the file so that we can start the reading of the bin file from the beginning
 *                                  point.
 * 
 * Constructors: Just the default constructor; takes no arguments.
 * 
 * Instance Methods:
 *                - String getState()
 *                - String getStateFIPS() 
 *                - String getNaicsDescription()
 *                - String getEmploymentNoiseFlag()
 *                - String getFirstQuarterPayrollNoiseFlag()
 *                - String getAnnualPayrollNoiseFlag()
 *                - String getCongressionalDistrict()
 *                - String getCodeNAICS()
 *                - int getNumOfEstablishments()
 *                - int getEmployment()
 *                - int getFirstQuarterPayroll()
 *                - int getAnnualPayroll()
 *                - void printRecord()
 *                - dumpObject( RandomAccessFile stream, int[] fieldLengths )
 * 
 * Class Methods:
 *                - List<Integer) getStringFieldLengths( RandomAccessFile binFile )
 *                - CSVRecord fetchRecord( RandomAccessFile binFile, List<Integer) stringFieldLengths, int startingLocation )
 *                - CSVRecord fetchRecord( RandomAccessFile binFile, List<Integer) stringFieldLengths )
 *                - void fillCSVRecordObject(CSVRecord record, RandomAccessFile binFile, List<Integer> fieldLengths, int startPlace)
 *                - int compareTo(CSVRecord record)
 * 
 */
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class CSVRecord {

    private static final int BEGINNINGOFFILE = 0;


    private String state;
    private String naicsDescription;
    private String employmentNoiseFlag;
    private String firstQuarterPayrollNoiseFlag;
    private String annualPayrollNoiseFlag;
    private String stateFIPS;
    private String congressionalDistrict;
    private String codeNAICS;

    private int numOfEstablishments;
    private int employment;
    private int firstQuarterPayroll;
    private int annualPayroll;

    public CSVRecord() {

    }

    public String getStateFIPS() {
        return stateFIPS;
    }

    public void setStateFIPS(String stateFIPS) {
        this.stateFIPS = stateFIPS;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNaicsDescription() {
        return naicsDescription;
    }

    public void setNaicsDescription(String naicsDescription) {
        this.naicsDescription = naicsDescription;
    }

    public String getEmploymentNoiseFlag() {
        return employmentNoiseFlag;
    }

    public void setEmploymentNoiseFlag(String employmentNoiseFlag) {
        this.employmentNoiseFlag = employmentNoiseFlag;
    }

    public String getFirstQuarterPayrollNoiseFlag() {
        return firstQuarterPayrollNoiseFlag;
    }

    public void setFirstQuarterPayrollNoiseFlag(String firstQuarterPayrollNoiseFlag) {
        this.firstQuarterPayrollNoiseFlag = firstQuarterPayrollNoiseFlag;
    }

    public String getAnnualPayrollNoiseFlag() {
        return annualPayrollNoiseFlag;
    }

    public void setAnnualPayrollNoiseFlag(String annualPayrollNoiseFlag) {
        this.annualPayrollNoiseFlag = annualPayrollNoiseFlag;
    }

    public String getCongressionalDistrict() {
        return congressionalDistrict;
    }

    public void setCongressionalDistrict(String congressionalDistrict) {
        this.congressionalDistrict = congressionalDistrict;
    }

    public String getCodeNAICS() {
        return codeNAICS;
    }

    public void setCodeNAICS(String codeNAICS) {
        this.codeNAICS = codeNAICS;
    }

    public int getNumOfEstablishments() {
        return numOfEstablishments;
    }

    public void setNumOfEstablishments(int numOfEstablishments) {
        this.numOfEstablishments = numOfEstablishments;
    }

    public int getEmployment() {
        return employment;
    }

    public void setEmployment(int employment) {
        this.employment = employment;
    }

    public int getFirstQuarterPayroll() {
        return firstQuarterPayroll;
    }

    public void setFirstQuarterPayroll(int firstQuarterPayroll) {
        this.firstQuarterPayroll = firstQuarterPayroll;
    }

    public int getAnnualPayroll() {
        return annualPayroll;
    }

    public void setAnnualPayroll(int annualPayroll) {
        this.annualPayroll = annualPayroll;
    }

    /**
     * Prints the values within the current object, assumes that the current object is not null
     */
    public void printRecord() {
        System.out.println("State Fips: " + getStateFIPS());
        System.out.println("State Name: " + getState());
        System.out.println("Congressional District: " + getCongressionalDistrict());
        System.out.println("2017 NAICS Code: " + getCodeNAICS());
        System.out.println("NAICS Description: " + getNaicsDescription());
        System.out.println("Number of Establishments: " + getNumOfEstablishments());
        System.out.println("Employment: " + getEmployment());
        System.out.println("Employment Noise Flag: " + getEmploymentNoiseFlag());
        System.out.println("First Quarter Payroll ($1000): " + getFirstQuarterPayroll());
        System.out.println("First Quarter Payroll Noise Flag: " + getFirstQuarterPayrollNoiseFlag());
        System.out.println("Annual Payroll ($1000): " + getAnnualPayroll());
        System.out.println("Annual Payroll Noise Flag: " + getAnnualPayrollNoiseFlag());
        System.out.println();
    }

    public void printRecordPartB() {
        System.out.printf("[%s][%s][%s][%d][%s]\n", getStateFIPS(), getCongressionalDistrict(), 
        getCodeNAICS(), getEmployment(), getEmploymentNoiseFlag() );
    }

    /**
     * Writes the current Record object to bin file in the following order.
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
     * @param stream
     * @param fieldLengths: An array of lenghts of longest strings from each string
     *                      field
     */
    public void dumpObject(RandomAccessFile stream, int[] fieldLengths) {
        // The strings need to get a StringBuffer in order to pad values correctly
        StringBuffer allBuffer = null;
        int currentIndex = 0;
        try {
            // Write the state FIPS to bin file
            allBuffer = new StringBuffer(getStateFIPS());
            allBuffer.setLength(fieldLengths[currentIndex]);
            stream.writeBytes(allBuffer.toString());
            currentIndex++;

            // Write the state name to bin file
            allBuffer = new StringBuffer(getState());
            allBuffer.setLength(fieldLengths[currentIndex]);
            stream.writeBytes(allBuffer.toString());
            currentIndex++;

            // Write congressional district to bin file
            allBuffer = new StringBuffer(getCongressionalDistrict());
            allBuffer.setLength(fieldLengths[currentIndex]);
            stream.writeBytes(allBuffer.toString());
            currentIndex++;

            // Write 2017 NAICS Code
            allBuffer = new StringBuffer(getCodeNAICS());
            allBuffer.setLength(fieldLengths[currentIndex]);
            stream.writeBytes(allBuffer.toString());
            currentIndex++;

            // Write the NAICS Description
            allBuffer = new StringBuffer(getNaicsDescription());
            allBuffer.setLength(fieldLengths[currentIndex]);
            stream.writeBytes(allBuffer.toString());
            currentIndex++;

            // Write number of establishments
            stream.writeInt(getNumOfEstablishments());

            // Write employment
            stream.writeInt(getEmployment());

            // Write Employment Noise Flag
            allBuffer = new StringBuffer(getEmploymentNoiseFlag());
            allBuffer.setLength(fieldLengths[currentIndex]);
            stream.writeBytes(allBuffer.toString());
            currentIndex++;

            // Write 1st Quarter Payroll
            stream.writeInt(getNumOfEstablishments());

            // Write 1st Quarter Payroll Noise Flag
            allBuffer = new StringBuffer(getFirstQuarterPayrollNoiseFlag());
            allBuffer.setLength(fieldLengths[currentIndex]);
            stream.writeBytes(allBuffer.toString());
            currentIndex++;

            // Write Annual Payroll
            stream.writeInt(getAnnualPayroll());

            // Write Annual Payroll Noise Flag
            allBuffer = new StringBuffer(getAnnualPayrollNoiseFlag());
            allBuffer.setLength(fieldLengths[currentIndex]);
            stream.writeBytes(allBuffer.toString());
        } catch (IOException e) {
            System.out.println("I/O ERROR: Couldn't write to the file;\n\t"
                    + "perhaps the file system is full?");
            System.exit(-1);
        }
    }

    /**
     * Opens a binFile and fills out an ArrayList of Integers of all the lengths of the string fields 
     * for this object. It reads in the order of String from left to write within the CSV/bin file.
     * This is to read the last 32 bytes of the file since the lengths of each string is stored at the 
     * end of the bin file
     * @param binFile
     * @return ArrayList<Integer> the string field lengths for the string fields for CSVRecord object.
     */
    public static ArrayList<Integer> getStringFieldLengths(RandomAccessFile binFile, long numberOfBytesInFile ) {
        ArrayList<Integer> stringFieldLengths = new ArrayList<>();
        
        // We know the final 32 bytes are all the lengths of the string fields since that's how we wrote it.
        // also know that the numberOfBytesInFile doesn't include the string lengths fields so we can go past it
        try  {
            // Seek the just after the last record
            binFile.seek(numberOfBytesInFile);

            // Now get each of the eight string field lengths and put them in the arraylist to return
            for ( int i = 0; i < 8; i++ ) {
                int stringLength = binFile.readInt();
                stringFieldLengths.add(stringLength);
            }
        } catch ( IOException e ) {
            System.out.println("Error: There was an error retrieving the string field lengths.");
            System.exit(-1);
        }
        
  
        return stringFieldLengths;
    }

    /**
     * This method is used when wanting to create a CSVRecord object from the binFile generated from the dumpObject
     * method. Will read through the binFile assuming the stringFieldLengths are correctly placed at the end of the file.
     * Calls the method to fill a new CSVRecord starting at the beginning of the file.
     * @param binFile
     * @param stringFieldLengths
     * @return A new CSVRecord object created from a binFile
     */
    public static CSVRecord fetchObject(RandomAccessFile binFile, List<Integer> stringFieldLengths) {
        CSVRecord record = new CSVRecord();

        fillCSVRecordObject(record, binFile, stringFieldLengths, BEGINNINGOFFILE );

        return record;
    }

    /**
     * This method is to be used when executing a search within a binFile. Like the other fetchObject method this 
     * function returns a new CSVRecord object however, the user can specify which byte to start at when reading the file
     * in order to return a specific record from the bin file.
     * @param binFile
     * @param stringFieldLengths
     * @param startPlace
     * @return A new CSVRecord object created from a binFile starting a specific byte.
     */
    public static CSVRecord fetchObject(RandomAccessFile binFile, List<Integer> stringFieldLengths, int startPlace) {
        CSVRecord record = new CSVRecord();

        fillCSVRecordObject(record, binFile, stringFieldLengths, startPlace);

        return record;
    }


    /**
     * Fills the CSVRecord object from the bin file in the following order.
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
     * @param record:       CSVRecord object to be filled out from the binFile
     * @param fieldLengths: ArrayList of the various string field lenghts
     * @param binFile:      RandomAccessFile that is being read from to fill out the CSVRecord object
     */
    private static void fillCSVRecordObject(CSVRecord record, RandomAccessFile binFile, List<Integer> fieldLengths,
    int startPlace) {

        try {
            binFile.seek(startPlace);
        } catch (IOException e ) {
            System.out.println("There was an error placing the cursor down.");
            System.exit(-1);
        }

        int currentStringFieldIndex = 0;

        // Creating the byte arrays for each of the string fields
        byte[] stateFIPS = new byte[fieldLengths.get(currentStringFieldIndex)];
        currentStringFieldIndex++;

        byte[] stateName = new byte[fieldLengths.get(currentStringFieldIndex)];
        currentStringFieldIndex++;

        byte[] district = new byte[fieldLengths.get(currentStringFieldIndex)];
        currentStringFieldIndex++;

        byte[] naicsCode = new byte[fieldLengths.get(currentStringFieldIndex)];
        currentStringFieldIndex++;

        byte[] description = new byte[fieldLengths.get(currentStringFieldIndex)];
        currentStringFieldIndex++;

        byte[] employmentNoiseFlag = new byte[fieldLengths.get(currentStringFieldIndex)];
        currentStringFieldIndex++;

        byte[] firstQuarterNoiseFlag = new byte[fieldLengths.get(currentStringFieldIndex)];
        currentStringFieldIndex++;

        byte[] annualNoiseFlag = new byte[fieldLengths.get(currentStringFieldIndex)];

        try {
            binFile.readFully(stateFIPS);
            record.setStateFIPS(new String(stateFIPS));

            binFile.readFully(stateName);
            record.setState(new String(stateName));

            binFile.readFully(district);
            record.setCongressionalDistrict(new String(district));

            binFile.readFully(naicsCode);
            record.setCodeNAICS(new String(naicsCode));

            binFile.readFully(description);
            record.setNaicsDescription(new String(description));

            record.setNumOfEstablishments(binFile.readInt());

            record.setEmployment(binFile.readInt());

            binFile.readFully(employmentNoiseFlag);
            record.setEmploymentNoiseFlag(new String(employmentNoiseFlag));

            record.setFirstQuarterPayroll(binFile.readInt());

            binFile.readFully(firstQuarterNoiseFlag);

            record.setAnnualPayroll(binFile.readInt());

            binFile.readFully(annualNoiseFlag);
            record.setAnnualPayrollNoiseFlag(new String(annualNoiseFlag));

        } catch (IOException e) {
            System.out.println("There was an error reading from the bin file");
            System.exit(-1);
        }

    }

    /**
     * This is similar to the regular CompareTo method. Specically created to follow the requirement of objects being 
     * compared based off their concatenation of State FIPS, Congressional District, and NAICS Code.
     * @param record
     * @return Returns negative number if this object is less than the one its being compared to. Zero if the objects are the 
     *          same, and a positive number if this object comes after the one it is being compared to.
     */
    public int compareTo(CSVRecord record) {
        if ( record == null) {
            System.out.println("The object being compared to this one is null");
            return -1;
        }
        String firstConcat = this.getStateFIPS() + this.getCongressionalDistrict() + this.getCodeNAICS();
        String secondConcat = record.getStateFIPS() + record.getCongressionalDistrict() + record.getCodeNAICS();

        return firstConcat.compareTo(secondConcat);
    }

    /**
     * Grabs the concatenation that matches input format from this CSVRecord object.
     * @return Concatenation of State FIPS, Congressional District, and NAICS Code
     */
    public String grabConcatentation() {
        return getStateFIPS() + getCongressionalDistrict() + getCodeNAICS();
    }


}
