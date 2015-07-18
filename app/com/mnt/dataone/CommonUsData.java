
package com.mnt.dataone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "basic_data",
    "pricing",
    "engines",
    "transmissions",
    "specifications",
    "installed_equipment"
})
public class CommonUsData {

    @JsonProperty("basic_data")
    private BasicData basicData;
    @JsonProperty("pricing")
    private Pricing pricing;
    @JsonProperty("engines")
    private List<Engine> engines = new ArrayList<Engine>();
    @JsonProperty("transmissions")
    private List<Transmission> transmissions = new ArrayList<Transmission>();
    @JsonProperty("specifications")
    private List<Specification> specifications = new ArrayList<Specification>();
    @JsonProperty("installed_equipment")
    private List<InstalledEquipment> installedEquipment = new ArrayList<InstalledEquipment>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The basicData
     */
    @JsonProperty("basic_data")
    public BasicData getBasicData() {
        return basicData;
    }

    /**
     * 
     * @param basicData
     *     The basic_data
     */
    @JsonProperty("basic_data")
    public void setBasicData(BasicData basicData) {
        this.basicData = basicData;
    }

    /**
     * 
     * @return
     *     The pricing
     */
    @JsonProperty("pricing")
    public Pricing getPricing() {
        return pricing;
    }

    /**
     * 
     * @param pricing
     *     The pricing
     */
    @JsonProperty("pricing")
    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    /**
     * 
     * @return
     *     The engines
     */
    @JsonProperty("engines")
    public List<Engine> getEngines() {
        return engines;
    }

    /**
     * 
     * @param engines
     *     The engines
     */
    @JsonProperty("engines")
    public void setEngines(List<Engine> engines) {
        this.engines = engines;
    }

    /**
     * 
     * @return
     *     The transmissions
     */
    @JsonProperty("transmissions")
    public List<Transmission> getTransmissions() {
        return transmissions;
    }

    /**
     * 
     * @param transmissions
     *     The transmissions
     */
    @JsonProperty("transmissions")
    public void setTransmissions(List<Transmission> transmissions) {
        this.transmissions = transmissions;
    }

    /**
     * 
     * @return
     *     The specifications
     */
    @JsonProperty("specifications")
    public List<Specification> getSpecifications() {
        return specifications;
    }

    /**
     * 
     * @param specifications
     *     The specifications
     */
    @JsonProperty("specifications")
    public void setSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
    }

    /**
     * 
     * @return
     *     The installedEquipment
     */
    @JsonProperty("installed_equipment")
    public List<InstalledEquipment> getInstalledEquipment() {
        return installedEquipment;
    }

    /**
     * 
     * @param installedEquipment
     *     The installed_equipment
     */
    @JsonProperty("installed_equipment")
    public void setInstalledEquipment(List<InstalledEquipment> installedEquipment) {
        this.installedEquipment = installedEquipment;
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
