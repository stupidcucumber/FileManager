package backend;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;

public class CopyMaker {
    private final CustomFile file;
    private final CustomFile copy;

    public CopyMaker(CustomFile file, CustomFile copy) {
        this.file = file;
        this.copy = copy;
    }

    public void copy() throws IOException {
        Scanner scanner = new Scanner(file);
        FileWriter fileWriter = new FileWriter(copy);

        String previous = scanner.nextLine();
        fileWriter.write(previous + "\n");
        while (scanner.hasNext()) {
            String temp = scanner.nextLine();
            if (!temp.equals(previous)) {
                fileWriter.write(temp + "\n");
                previous = String.copyValueOf(temp.toCharArray());
            }
        }

        fileWriter.close();
        scanner.close();
    }

    public void copyFile() throws IOException {
        Scanner scanner = new Scanner(file);
        FileWriter fileWriter = new FileWriter(file);

        while (scanner.hasNextLine()) {
            String row = scanner.nextLine() + "\n";
            fileWriter.write(row);
        }

        fileWriter.close();
        scanner.close();
    }
}
