package classes;

public class Page {
    private String a;
    private String body;
    private String title;
    private String h;

    public Page(String a, String body, String title, String h) {
        this.a = a;
        this.body = body;
        this.title = title;
        this.h = h;
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
}


