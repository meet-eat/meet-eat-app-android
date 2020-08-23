package meet_eat.app.repository;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import meet_eat.app.MeetEatApplication;

/**
 * Represents the unit for handling files.
 */
public final class FileHandler {

    private static final String ERROR_MESSAGE_NULL_CONTEXT = "No accessible application file directory available.";
    private static final String ERROR_MESSAGE_NOT_DELETED = "File was not deleted due to an error.";
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * Creates a {@link FileHandler file handler}.
     */
    private FileHandler() {
    }

    /**
     * Saves a {@link String} to a file.
     *
     * @param fileContent The {@link String} to be saved
     * @param fileName    the name of the file to save the content to
     * @throws IOException if an I/O error occurs
     */
    public static void saveStringToFile(String fileContent, String fileName) throws IOException {
        Context context = MeetEatApplication.getAppContext();
        if (Objects.isNull(context)) {
            throw new IOException(ERROR_MESSAGE_NULL_CONTEXT);
        }
        Objects.requireNonNull(fileContent);
        Objects.requireNonNull(fileName);
        try (FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(fileContent.getBytes());
        }
    }

    /**
     * Returns a {@link String} read from a file.
     *
     * @param fileName The name of the file the {@link String} is read from.
     * @return a {@link String} read from the file
     * @throws IOException if an I/O error occurs
     */
    public static String readFileToString(String fileName) throws IOException {
        FileInputStream fileInputStream = getContext().openFileInput(Objects.requireNonNull(fileName));
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, CHARSET))) {
            String line;
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Returns a {@link String} read from an {@link InputStream}.
     *
     * @param inputStream the stream the {@link String} is read from
     * @return the {@link String} read from the {@link InputStream}
     * @throws IOException if an I/O error occurs
     */
    public static String readInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, CHARSET))) {
            String line;
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Deletes a file.
     *
     * @param fileName the name of the file to be deleted
     * @throws IOException if an I/O error occurs
     */
    public static void deleteFile(String fileName) throws IOException {
        if (!getContext().deleteFile(Objects.requireNonNull(fileName))) {
            throw new IOException(ERROR_MESSAGE_NOT_DELETED);
        }
    }

    /**
     * Tests whether the file with the given name exists.
     *
     * @param fileName the name of the file to check if it exists
     * @return {@code true} if and only if the file with the given name exists; {@code false} otherwise
     * @throws IOException
     */
    public static boolean fileExists(String fileName) throws IOException {
        File file = new File(getContext().getFilesDir(), Objects.requireNonNull(fileName));
        return file.exists();
    }

    /**
     * Returns the {@link Context context} of the application.
     *
     * @return the context of the application
     * @throws IOException if an I/O error occurs
     */
    private static Context getContext() throws IOException {
        Context context = MeetEatApplication.getAppContext();
        if (Objects.isNull(context)) {
            throw new IOException(ERROR_MESSAGE_NULL_CONTEXT);
        }
        return context;
    }
}
