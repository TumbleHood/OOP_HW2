package api;

public class GL implements GeoLocation{

    private final double x;
    private final double y;
    private final double z;

    public GL(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(GeoLocation g) {
        return Math.sqrt(Math.pow(this.x() - g.x(), 2) + Math.pow(this.y() - g.y(), 2) + Math.pow(this.z() - g.z(), 2));
    }
}
