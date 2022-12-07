package windows.editor;

public class TextEditorFactory implements EditorFactory{
    @Override
    public Editor instantiateEditor() {
        return new TextEditor();
    }
}
