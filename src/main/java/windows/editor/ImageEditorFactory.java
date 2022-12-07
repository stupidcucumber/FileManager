package windows.editor;

public class ImageEditorFactory implements EditorFactory{
    @Override
    public Editor instantiateEditor() {
        return new ImageEditor();
    }
}
