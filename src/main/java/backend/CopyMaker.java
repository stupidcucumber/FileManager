package backend;

import windows.FileCopy;

import java.util.ArrayList;
import java.util.List;

public class CopyMaker {
    private final CustomFile file;

    public CopyMaker(CustomFile file) {
        this.file = file;
    }

    public static void copy() {
        String path = FileCopy.launch();
    }
}
