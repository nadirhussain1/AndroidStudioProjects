
package com.brainpixel.valetapp.model.help;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpContensResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private List<HelpDataItem> data = null;

    public List<HelpDataItem> getData() {
        return data;
    }

    public void setData(List<HelpDataItem> data) {
        this.data = data;
    }

}
