package CellSociety;

/**
 * Created by th174 on 1/29/2017.
 */
public class WindowProperties {
    private static double width;
    private static double height;

    /**
     * Set window dimensions
     * @param w = window width
     * @param h = window height
     */
    public static void setDimensions(double w, double h) {
        width = w;
        height = h;
    }

    /**
     * @return window width as double
     */
    public static double getWidth() {
        return width;
    }

    /**
     * @return window height as double
     */
    public static double getHeight() {
        return height;
    }
}
