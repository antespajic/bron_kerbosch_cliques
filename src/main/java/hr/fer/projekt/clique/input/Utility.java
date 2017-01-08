package hr.fer.projekt.clique.input;

/**
 * Utility class for assistance during file manipulation.
 */
public class Utility {

    /**
     * Retrieves file extension from file name, or returns
     * null if file name contains no extension.
     *
     * @param fileName file name
     * @return file extension or null if no extension exists
     * @throws IllegalArgumentException if file name given is null
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("File name given can not be null.");
        }

        int dotIndex = fileName.indexOf(".");
        if (dotIndex == -1 || fileName.length() == (dotIndex + 1)) {
            return null;
        }
        return fileName.substring(dotIndex + 1);
    }
}
