import java.io.*;
import java.util.ArrayList;

public class Prog1A {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a csv file to read");
            System.exit(1);
        }

        String filePath = args[0];
        ArrayList<CSVRecord> records = readCSV(filePath);
        for (CSVRecord record : records ){
            record.printRecord();
        }
    }

    private void writeBinaryFile(RandomAccessFile file) {

    }

    /**
     * Read the CSV file that is passed in and create CSVRecord objects to store information on each line.
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
     * After filling out the CSVRecord Object store into the return ArrayList
     * @param filePath: String containing the path to the csv file to read
     * @return An ArrayList of CSVRecord created from the CSV File.
     */
    private static ArrayList<CSVRecord> readCSV(String filePath) {

        File file = null;
        BufferedReader reader = null;
        ArrayList<CSVRecord> records = new ArrayList<>();

        try {
            file = new File(filePath);
            reader = new BufferedReader(new FileReader(file));

            // Read the line out of the beginning so we can start looking at the data in the loop
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                CSVRecord record = new CSVRecord();
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Set the field variable in the CSVRecord Object we created.

                record.setStateFIPS(data[0]);
                record.setState(data[1]);
                record.setCongressionalDistrict(data[2]);
                record.setCodeNAICS(data[3].replace("-", ""));
                record.setNaicsDescription( data[4].replace("\"", "") );
                record.setNumOfEstablishments( Integer.parseInt(data[5]));
                record.setEmployment( Integer.parseInt(data[6]));
                record.setEmploymentNoiseFlag(data[7]);
                record.setFirstQuarterPayroll( Integer.parseInt(data[8]));
                record.setFirstQuarterPayrollNoiseFlag(data[9]);
                record.setAnnualPayroll( Integer.parseInt(data[10]));
                record.setAnnualPayrollNoiseFlag(data[11]);

                // Add record to list to return
                records.add(record);
            }

            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return records;
    }
}

// TODO: Need to write the description of the class CSVRecord
/*+----------------------------------------------------------------------
 ||
 ||  Class CSVRecord
 ||
 ||         Author:  Francisco Gonzalez
 ||
 ||        Purpose:  An object of this class holds the field values of one
 ||                  record of data.  There are three fields:  State Code,
 ||                  Place Code, and County Name.  These are motivated by
 ||                  the Federal Information Processing System (FIPS)
 ||                  geographic encoding system.
 ||
 ||  Inherits From:  None.
 ||
 ||     Interfaces:  None.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||      Constants:  RECORD_LENGTH -- the # of bytes required to hold
 ||                      the field values of a single record.  Because the
 ||                      data is pre-supplied, we can pre-compute the total.
 ||                  COUNTY_NAME_LENGTH -- Each county name in the data can
 ||                      have a different length.  Again, as this is a sample
 ||                      program with fixed data, we predefine the max length.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||   Constructors:  Just the default constructor; no arguments.
 ||
 ||  Class Methods:  None.
 ||
 ||  Inst. Methods:     int getStateCode()
 ||                     int getPlaceCode()
 ||                  String getCountyName()
 ||                    void setStateCode(int newCode)
 ||                    void setPlaceCode(int newCode)
 ||                    void setCountyName(String newName)
 ||
 ||                    void dumpObject(RandomAccessFile stream)
 ||                    void fetchObject(RandomAccessFile stream)
 ||
 ++-----------------------------------------------------------------------*/
class CSVRecord {
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

    public void printRecord() {
        System.out.println("State Fips: " + getStateFIPS());
        System.out.println("State Name: " + getState());
        System.out.println("Congressional District: " + getCongressionalDistrict());
        System.out.println("2017 NAICS Code: " + getCodeNAICS());
        System.out.println("NAICS Description: " + getNaicsDescription());
        System.out.println("Number of Establishments: " + getNumOfEstablishments());
        System.out.println("Employment: " + getEmployment());
        System.out.println("Employment Noise Flag: " + getEmploymentNoiseFlag() );
        System.out.println("First Quarter Payroll ($1000): " + getFirstQuarterPayroll());
        System.out.println("First Quarter Payroll Noise Flag: " + getFirstQuarterPayrollNoiseFlag());
        System.out.println("Annual Payroll ($1000): " + getAnnualPayroll());
        System.out.println("Annual Payroll Noise Flag: " + getAnnualPayrollNoiseFlag());
        System.out.println();
    }
}
