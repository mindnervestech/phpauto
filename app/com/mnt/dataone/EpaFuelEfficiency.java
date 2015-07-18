
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
    "engine_id",
    "transmission_id",
    "fuel_type",
    "fuel_grade",
    "city",
    "highway",
    "combined"
})
public class EpaFuelEfficiency {

    @JsonProperty("engine_id")
    private String engineId;
    @JsonProperty("transmission_id")
    private String transmissionId;
    @JsonProperty("fuel_type")
    private String fuelType;
    @JsonProperty("fuel_grade")
    private String fuelGrade;
    @JsonProperty("city")
    private String city;
    @JsonProperty("highway")
    private String highway;
    @JsonProperty("combined")
    private String combined;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The engineId
     */
    @JsonProperty("engine_id")
    public String getEngineId() {
        return engineId;
    }

    /**
     * 
     * @param engineId
     *     The engine_id
     */
    @JsonProperty("engine_id")
    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    /**
     * 
     * @return
     *     The transmissionId
     */
    @JsonProperty("transmission_id")
    public String getTransmissionId() {
        return transmissionId;
    }

    /**
     * 
     * @param transmissionId
     *     The transmission_id
     */
    @JsonProperty("transmission_id")
    public void setTransmissionId(String transmissionId) {
        this.transmissionId = transmissionId;
    }

    /**
     * 
     * @return
     *     The fuelType
     */
    @JsonProperty("fuel_type")
    public String getFuelType() {
        return fuelType;
    }

    /**
     * 
     * @param fuelType
     *     The fuel_type
     */
    @JsonProperty("fuel_type")
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * 
     * @return
     *     The fuelGrade
     */
    @JsonProperty("fuel_grade")
    public String getFuelGrade() {
        return fuelGrade;
    }

    /**
     * 
     * @param fuelGrade
     *     The fuel_grade
     */
    @JsonProperty("fuel_grade")
    public void setFuelGrade(String fuelGrade) {
        this.fuelGrade = fuelGrade;
    }

    /**
     * 
     * @return
     *     The city
     */
    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    /**
     * 
     * @param city
     *     The city
     */
    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 
     * @return
     *     The highway
     */
    @JsonProperty("highway")
    public String getHighway() {
        return highway;
    }

    /**
     * 
     * @param highway
     *     The highway
     */
    @JsonProperty("highway")
    public void setHighway(String highway) {
        this.highway = highway;
    }

    /**
     * 
     * @return
     *     The combined
     */
    @JsonProperty("combined")
    public String getCombined() {
        return combined;
    }

    /**
     * 
     * @param combined
     *     The combined
     */
    @JsonProperty("combined")
    public void setCombined(String combined) {
        this.combined = combined;
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
