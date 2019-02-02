
package com.brainpixel.valetapp.model.settings;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralSettingsResponse  extends GeneralServerResponse{
    @SerializedName("data")
    @Expose
    private SettingsDataModel data;

    public SettingsDataModel getData() {
        return data;
    }

    public void setData(SettingsDataModel data) {
        this.data = data;
    }

}
