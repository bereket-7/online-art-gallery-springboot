package com.project.oag.app.dto;

public class StandardDto {
    private String standardDescription;
    private String standardType;

    public StandardDto() {
        super();
    }

    public StandardDto(String standardDescription, String standardType) {
        super();
        this.standardDescription = standardDescription;
        this.standardType = standardType;
    }

    public String getStandardDescription() {
        return standardDescription;
    }

    public void setStandardDescription(String standardDescription) {
        this.standardDescription = standardDescription;
    }

    public String getStandardType() {
        return standardType;
    }

    public void setStandardType(String standardType) {
        this.standardType = standardType;
    }

    @Override
    public String toString() {
        return "StandardDto [standardDescription=" + standardDescription + ", standardType=" + standardType + "]";
    }

}
