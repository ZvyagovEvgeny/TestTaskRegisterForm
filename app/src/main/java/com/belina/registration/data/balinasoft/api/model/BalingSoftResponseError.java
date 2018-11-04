
package com.belina.registration.data.balinasoft.api.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalingSoftResponseError {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("valid")
    @Expose
    private List<Valid> valid = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Valid> getValid() {
        return valid;
    }

    public void setValid(List<Valid> valid) {
        this.valid = valid;
    }

}
