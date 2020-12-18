package com.epump.epumpterminal.models;

public class DetailsToSend  {
    private Double latitude;
    private Double longitude;
    private String branchId;

    public DetailsToSend(Double latitude, Double longitude, String branchId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.branchId = branchId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
}
