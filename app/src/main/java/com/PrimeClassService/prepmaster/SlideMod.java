package com.PrimeClassService.prepmaster;

public class SlideMod {
    String slideName;
    String slidePath;
    String slideType;

    public SlideMod(String slideName, String slidePath, String slideType) {
        this.slideName = slideName;
        this.slidePath = slidePath;
        this.slideType = slideType;
    }

    public String getSlideName() {
        return slideName;
    }

    public void setSlideName(String slideName) {
        this.slideName = slideName;
    }

    public String getSlidePath() {
        return slidePath;
    }

    public void setSlidePath(String slidePath) {
        this.slidePath = slidePath;
    }

    public String getSlideType() {
        return slideType;
    }

    public void setSlideType(String slideType) {
        this.slideType = slideType;
    }
}
