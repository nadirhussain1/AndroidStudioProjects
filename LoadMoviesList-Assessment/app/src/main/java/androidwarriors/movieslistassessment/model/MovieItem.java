
package androidwarriors.movieslistassessment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MovieItem {

    private String skyGoUrl;
    private String url;
    private String reviewAuthor;
    private String id;
    private String cert;
    private ViewingWindow viewingWindow;
    private String headline;
    private List<CardImage> cardImages = new ArrayList<CardImage>();
    private List<Director> directors = new ArrayList<Director>();
    private String sum;
    private List<KeyArtImage> keyArtImages = new ArrayList<KeyArtImage>();
    private String synopsis;
    private String body;
    private List<Cast> cast = new ArrayList<Cast>();
    private String skyGoId;
    private String year;
    private Integer duration;
    private Integer rating;
    private String _class;
    private List<Video> videos = new ArrayList<Video>();
    private String lastUpdated;
    private List<String> genres = new ArrayList<String>();
    private String quote;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The skyGoUrl
     */
    public String getSkyGoUrl() {
        return skyGoUrl;
    }

    /**
     * 
     * @param skyGoUrl
     *     The skyGoUrl
     */
    public void setSkyGoUrl(String skyGoUrl) {
        this.skyGoUrl = skyGoUrl;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The reviewAuthor
     */
    public String getReviewAuthor() {
        return reviewAuthor;
    }

    /**
     * 
     * @param reviewAuthor
     *     The reviewAuthor
     */
    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The cert
     */
    public String getCert() {
        return cert;
    }

    /**
     * 
     * @param cert
     *     The cert
     */
    public void setCert(String cert) {
        this.cert = cert;
    }

    /**
     * 
     * @return
     *     The viewingWindow
     */
    public ViewingWindow getViewingWindow() {
        return viewingWindow;
    }

    /**
     * 
     * @param viewingWindow
     *     The viewingWindow
     */
    public void setViewingWindow(ViewingWindow viewingWindow) {
        this.viewingWindow = viewingWindow;
    }

    /**
     * 
     * @return
     *     The headline
     */
    public String getHeadline() {
        return headline;
    }

    /**
     * 
     * @param headline
     *     The headline
     */
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    /**
     * 
     * @return
     *     The cardImages
     */
    public List<CardImage> getCardImages() {
        return cardImages;
    }

    /**
     * 
     * @param cardImages
     *     The cardImages
     */
    public void setCardImages(List<CardImage> cardImages) {
        this.cardImages = cardImages;
    }

    /**
     * 
     * @return
     *     The directors
     */
    public List<Director> getDirectors() {
        return directors;
    }

    /**
     * 
     * @param directors
     *     The directors
     */
    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    /**
     * 
     * @return
     *     The sum
     */
    public String getSum() {
        return sum;
    }

    /**
     * 
     * @param sum
     *     The sum
     */
    public void setSum(String sum) {
        this.sum = sum;
    }

    /**
     * 
     * @return
     *     The keyArtImages
     */
    public List<KeyArtImage> getKeyArtImages() {
        return keyArtImages;
    }

    /**
     * 
     * @param keyArtImages
     *     The keyArtImages
     */
    public void setKeyArtImages(List<KeyArtImage> keyArtImages) {
        this.keyArtImages = keyArtImages;
    }

    /**
     * 
     * @return
     *     The synopsis
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * 
     * @param synopsis
     *     The synopsis
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * 
     * @return
     *     The body
     */
    public String getBody() {
        return body;
    }

    /**
     * 
     * @param body
     *     The body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 
     * @return
     *     The cast
     */
    public List<Cast> getCast() {
        return cast;
    }

    /**
     * 
     * @param cast
     *     The cast
     */
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    /**
     * 
     * @return
     *     The skyGoId
     */
    public String getSkyGoId() {
        return skyGoId;
    }

    /**
     * 
     * @param skyGoId
     *     The skyGoId
     */
    public void setSkyGoId(String skyGoId) {
        this.skyGoId = skyGoId;
    }

    /**
     * 
     * @return
     *     The year
     */
    public String getYear() {
        return year;
    }

    /**
     * 
     * @param year
     *     The year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * 
     * @return
     *     The duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     *     The rating
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * 
     * @return
     *     The _class
     */
    public String getClass_() {
        return _class;
    }

    /**
     * 
     * @param _class
     *     The class
     */
    public void setClass_(String _class) {
        this._class = _class;
    }

    /**
     * 
     * @return
     *     The videos
     */
    public List<Video> getVideos() {
        return videos;
    }

    /**
     * 
     * @param videos
     *     The videos
     */
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    /**
     * 
     * @return
     *     The lastUpdated
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * 
     * @param lastUpdated
     *     The lastUpdated
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * 
     * @return
     *     The genres
     */
    public List<String> getGenres() {
        return genres;
    }

    /**
     * 
     * @param genres
     *     The genres
     */
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    /**
     * 
     * @return
     *     The quote
     */
    public String getQuote() {
        return quote;
    }

    /**
     * 
     * @param quote
     *     The quote
     */
    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
