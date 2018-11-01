package classes;

public class Identifier {
    private long start;
    private long end;

    public Identifier(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
}
