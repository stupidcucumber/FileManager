package windows.pathgetter;

public class MergePathGetterFactory implements PathGetterFactory {
    @Override
    public PathGetter instantiatePathGetter() {
        return new MergePathGetter();
    }
}
