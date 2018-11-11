package classes;

public class Page {
    private String a;
    private String body;
    private String title;
    private String h;
    private long start;
    private long end;

    public Page(String a, String body, String title, String h) {
        this.a = a;
        this.body = body;
        this.title = title;
        this.h = h;
    }

    public Page(String a, String body, String title, String h, long start, long end) {
        this.a = a;
        this.body = body;
        this.title = title;
        this.h = h;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Page{" +
                "\n a ='" + a + '\'' +
                "\n body='" + body + '\'' +
                "\n title='" + title + '\'' +
                "\n h='" + h + '\'' +
                '}';
    }

    public String getA() {
        return a;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getH() {
        return h;
    }

    public void setA(String a) {
        this.a = a;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setH(String h) {
        this.h = h;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}


