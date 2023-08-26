import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class CSVRecord {
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
     *                       field
     */
    public void dumpObject(RandomAccessFile stream, int[] fieldLengths) {
        // The strings need to get a StringBuffer in order to pad values correctly
        StringBuffer allBuffer = null;
        int currentIndex = 0;
        try {

            // Write the lengths of the string fields at the beginning of the file
            for ( Integer number : fieldLengths ) {
                stream.writeInt(number);
            }
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

    public static void fetchObject(RandomAccessFile binFile ) {
        ArrayList<Integer> stringFieldLengths = new ArrayList<>();
        try {
            binFile.seek(0);
        } catch ( Exception e ) {
            System.out.println("Something crazy happened");
            System.exit(-1);
        }
        for (int i = 0; i < 8; i++) {
            try {
                int number = binFile.readInt();
                System.out.println(number);
            } catch (Exception e) {
                System.out.println("There was an error reading the string field lengths.");
                System.exit(-1);
            }
        }
        
    }
}
