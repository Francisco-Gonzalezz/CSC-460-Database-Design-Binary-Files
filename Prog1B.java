import java.io.File;
import java.io.RandomAccessFile;

public class Prog1B {

    public static void main ( String[] args ){
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

        grabStringLengths(filepath);

    }

    private static int[] grabStringLengths( String filepath ) {
        File file = null;
        int[] stringFieldLengths = new int[8];

        try {
            file = new File(filepath);
        } catch (Exception e) {
            System.out.println("There was an error with opening the bin file");
            System.exit(-1);
        }

        RandomAccessFile binFile = null;

        try {
            binFile = new RandomAccessFile(file, "r");
        } catch ( Exception e ) {
            System.out.println("Something went wrong with opening of the bin file");
        }

        CSVRecord.fetchObject(binFile);




        return stringFieldLengths;
    }
}
