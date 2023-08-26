public class PersonalUtil {


    /**
     * This function returns true if the file passed into it through filepath paramaeter has
     * the expected file extension and false otherwise.
     *
     * This function operates under the assumption that there are no forward slashes in the file name.
     * Another assumption of this function is that it is being called from a UNIX-like environment.
     * @param filepath
     * @param expectedExtension
     * @return
     */
    public static boolean verifyExtensionIsCorrect( String filepath, String expectedExtension) {

        // Split the string on forward slashes so we can extract the file from the path
        String[] filePathSplit = filepath.split("/");

        String filename = filePathSplit[filePathSplit.length - 1];

        String[] fileNameSplit = filename.split("\\.");

        String actualExtension = fileNameSplit[1];

        return actualExtension.equals(expectedExtension);
    }

}
