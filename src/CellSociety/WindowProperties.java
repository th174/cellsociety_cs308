package CellSociety;

/**
 * Created by th174 on 1/29/2017.
 */
public class WindowProperties {
    private static double width;
    private static double height;

    public static void setDimensions(double w, double h) {
        width = w;
        height = h;
    }

    public static double getWidth() {
        return width;
    }

    public static double getHeight() {
        return height;
    }

}
