package patagonia;

/**
 * Created by nadirhussain on 25/08/2016.
 */
public class SimpleBook {
    private String author;
    private String description;
    private String title;
    private String coverPhoto;
    private String eISBN;

    public SimpleBook(String title, String eISBN) {
        this.title = title;
        this.eISBN = eISBN;
        coverPhoto = "http://d14d56uiasjj4l.cloudfront.net/" + eISBN + "_cover.jpg";
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String geteISBN() {
        return eISBN;
    }

    public void seteISBN(String eISBN) {
        this.eISBN = eISBN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getCoverUrl(String eISBN){
        return "http://d14d56uiasjj4l.cloudfront.net/" + eISBN + "_cover.jpg";
    }

}
