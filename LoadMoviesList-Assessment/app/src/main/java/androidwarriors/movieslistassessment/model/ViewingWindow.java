
package androidwarriors.movieslistassessment.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ViewingWindow {

    private String startDate;
    private String wayToWatch;
    private String endDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return
     *     The wayToWatch
     */
    public String getWayToWatch() {
        return wayToWatch;
    }

    /**
     * 
     * @param wayToWatch
     *     The wayToWatch
     */
    public void setWayToWatch(String wayToWatch) {
        this.wayToWatch = wayToWatch;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
