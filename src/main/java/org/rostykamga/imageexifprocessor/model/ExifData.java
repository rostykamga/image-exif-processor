package org.rostykamga.imageexifprocessor.model;

import java.util.Date;
import java.util.Objects;

public class ExifData {

    private int imageHeight;
    private int imageWidth;
    private double xResolution;
    private double yResolution;
    private String resolutionUnit;
    private long imageLength;

    private Date datetime;
    private String make;
    private String model;
    private String copyright;

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public double getxResolution() {
        return xResolution;
    }

    public void setxResolution(double xResolution) {
        this.xResolution = xResolution;
    }

    public double getyResolution() {
        return yResolution;
    }

    public void setyResolution(double yResolution) {
        this.yResolution = yResolution;
    }

    public String getResolutionUnit() {
        return resolutionUnit;
    }

    public void setResolutionUnit(String resolutionUnit) {
        this.resolutionUnit = resolutionUnit;
    }

    public long getImageLength() {
        return imageLength;
    }

    public void setImageLength(long imageLength) {
        this.imageLength = imageLength;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Checks if no property of this object has been set
     * @return true if no property set, false otherwise.
     */
    public boolean isEmpty(){

        return this.copyright == null && this.datetime == null && this.imageHeight == 0
                && this.imageLength == 0 && this.imageWidth == 0 && this.make == null && this.model == null
                && this.resolutionUnit == null && this.yResolution == 0 && this.xResolution == 0;

    }

    @Override
    public String toString() {
        return "ExifData{" +
                "imageHeight=" + imageHeight +
                ", imageWidth=" + imageWidth +
                ", xResolution=" + xResolution +
                ", yResolution=" + yResolution +
                ", resolutionUnit='" + resolutionUnit + '\'' +
                ", imageLength=" + imageLength +
                ", datetime=" + datetime +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", copyright='" + copyright + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExifData exifData = (ExifData) o;
        return imageHeight == exifData.imageHeight && imageWidth == exifData.imageWidth && Double.compare(exifData.xResolution, xResolution) == 0 && Double.compare(exifData.yResolution, yResolution) == 0 && imageLength == exifData.imageLength && Objects.equals(resolutionUnit, exifData.resolutionUnit) && Objects.equals(datetime, exifData.datetime) && Objects.equals(make, exifData.make) && Objects.equals(model, exifData.model) && Objects.equals(copyright, exifData.copyright);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageHeight, imageWidth, xResolution, yResolution, resolutionUnit, imageLength, datetime, make, model, copyright);
    }
}
