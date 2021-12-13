package api;

public class ED implements EdgeData{

    private final int src;
    private final int dest;
    private final double weight;
    private String info;
    private int tag;

    public ED(int s, int d, double w){
        this.src = s;
        this.dest = d;
        this.weight = w;
        this.info = null;
        this.tag = 0;
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
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
