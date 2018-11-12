package classes;

public class Identifier {
    private long start;
    private long end;

    public Identifier(long start, long end) {
        this.start = start;
        this.end = end;
    }
    public Identifier(String start, String end) {
        this.start = Long.parseLong(start);
        this.end = Long.parseLong(end);
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
}
