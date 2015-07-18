
package com.mnt.dataone;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "mfr_code",
    "two_tone",
    "generic_color_name",
    "mfr_color_name",
    "primary_rgb_code",
    "secondary_rgb_code"
})
public class InteriorColor {

    @JsonProperty("mfr_code")
    private String mfrCode;
    @JsonProperty("two_tone")
    private String twoTone;
    @JsonProperty("generic_color_name")
    private String genericColorName;
    @JsonProperty("mfr_color_name")
    private String mfrColorName;
    @JsonProperty("primary_rgb_code")
    private PrimaryRgbCode_ primaryRgbCode;
    @JsonProperty("secondary_rgb_code")
    private SecondaryRgbCode_ secondaryRgbCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The mfrCode
     */
    @JsonProperty("mfr_code")
    public String getMfrCode() {
        return mfrCode;
    }

    /**
     * 
     * @param mfrCode
     *     The mfr_code
     */
    @JsonProperty("mfr_code")
    public void setMfrCode(String mfrCode) {
        this.mfrCode = mfrCode;
    }

    /**
     * 
     * @return
     *     The twoTone
     */
    @JsonProperty("two_tone")
    public String getTwoTone() {
        return twoTone;
    }

    /**
     * 
     * @param twoTone
     *     The two_tone
     */
    @JsonProperty("two_tone")
    public void setTwoTone(String twoTone) {
        this.twoTone = twoTone;
    }

    /**
     * 
     * @return
     *     The genericColorName
     */
    @JsonProperty("generic_color_name")
    public String getGenericColorName() {
        return genericColorName;
    }

    /**
     * 
     * @param genericColorName
     *     The generic_color_name
     */
    @JsonProperty("generic_color_name")
    public void setGenericColorName(String genericColorName) {
        this.genericColorName = genericColorName;
    }

    /**
     * 
     * @return
     *     The mfrColorName
     */
    @JsonProperty("mfr_color_name")
    public String getMfrColorName() {
        return mfrColorName;
    }

    /**
     * 
     * @param mfrColorName
     *     The mfr_color_name
     */
    @JsonProperty("mfr_color_name")
    public void setMfrColorName(String mfrColorName) {
        this.mfrColorName = mfrColorName;
    }

    /**
     * 
     * @return
     *     The primaryRgbCode
     */
    @JsonProperty("primary_rgb_code")
    public PrimaryRgbCode_ getPrimaryRgbCode() {
        return primaryRgbCode;
    }

    /**
     * 
     * @param primaryRgbCode
     *     The primary_rgb_code
     */
    @JsonProperty("primary_rgb_code")
    public void setPrimaryRgbCode(PrimaryRgbCode_ primaryRgbCode) {
        this.primaryRgbCode = primaryRgbCode;
    }

    /**
     * 
     * @return
     *     The secondaryRgbCode
     */
    @JsonProperty("secondary_rgb_code")
    public SecondaryRgbCode_ getSecondaryRgbCode() {
        return secondaryRgbCode;
    }

    /**
     * 
     * @param secondaryRgbCode
     *     The secondary_rgb_code
     */
    @JsonProperty("secondary_rgb_code")
    public void setSecondaryRgbCode(SecondaryRgbCode_ secondaryRgbCode) {
        this.secondaryRgbCode = secondaryRgbCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
