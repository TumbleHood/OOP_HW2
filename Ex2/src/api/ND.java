package api;

public class ND implements NodeData{

    private final int key;
    private GeoLocation location;
    private double weight;
    private String info;
    private int tag;

    public ND(int k){
        this.key = k;
        this.location = null;
        this.weight = 0;
        this.info = null;
        this.tag = 0;
    }
    public ND(int k, GeoLocation l, double w, String i, int t){
        this.key = k;
        this.location = l;
        this.weight = w;
        this.info = i;
        this.tag = t;
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }
}
