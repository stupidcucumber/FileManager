package backend;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Merger {
    private final CustomFile customFile_left;
    private final CustomFile customFile_right;

    private final CustomFile merged;

    public Merger(CustomFile customFile_left, CustomFile customFile_right,
                  CustomFile merged) {
        this.customFile_left = customFile_left;
        this.customFile_right = customFile_right;
        this.merged = merged;
    }

    public void merge() {
        try {
            rewriteFile(customFile_left);
            rewriteFile(customFile_right);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rewriteFile(CustomFile customFile) throws IOException {
        Scanner scanner = new Scanner(customFile);
        FileWriter fileWriter = new FileWriter(merged, true);
        while (scanner.hasNext()) {
            String row = scanner.nextLine();
            fileWriter.write(row + "\n");
        }

        fileWriter.close();
        scanner.close();
    }
}
