
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
    "r",
    "g",
    "b",
    "hex"
})
public class PrimaryRgbCode_ {

    @JsonProperty("r")
    private String r;
    @JsonProperty("g")
    private String g;
    @JsonProperty("b")
    private String b;
    @JsonProperty("hex")
    private String hex;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The r
     */
    @JsonProperty("r")
    public String getR() {
        return r;
    }

    /**
     * 
     * @param r
     *     The r
     */
    @JsonProperty("r")
    public void setR(String r) {
        this.r = r;
    }

    /**
     * 
     * @return
     *     The g
     */
    @JsonProperty("g")
    public String getG() {
        return g;
    }

    /**
     * 
     * @param g
     *     The g
     */
    @JsonProperty("g")
    public void setG(String g) {
        this.g = g;
    }

    /**
     * 
     * @return
     *     The b
     */
    @JsonProperty("b")
    public String getB() {
        return b;
    }

    /**
     * 
     * @param b
     *     The b
     */
    @JsonProperty("b")
    public void setB(String b) {
        this.b = b;
    }

    /**
     * 
     * @return
     *     The hex
     */
    @JsonProperty("hex")
    public String getHex() {
        return hex;
    }

    /**
     * 
     * @param hex
     *     The hex
     */
    @JsonProperty("hex")
    public void setHex(String hex) {
        this.hex = hex;
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
