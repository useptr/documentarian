package placeholders;

public class Point3D {
    private static volatile transient double x = 0;
    private double y = 0;
    public  double z = 0;
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
