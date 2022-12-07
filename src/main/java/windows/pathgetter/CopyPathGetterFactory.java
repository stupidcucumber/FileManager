package windows.pathgetter;

public class CopyPathGetterFactory implements PathGetterFactory {
    @Override
    public PathGetter instantiatePathGetter() {
        return new CopyPathGetter();
    }
}
