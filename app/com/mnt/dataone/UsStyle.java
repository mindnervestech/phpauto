
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
    "name",
    "vehicle_id",
    "complete",
    "market",
    "fleet",
    "basic_data",
    "pricing",
    "engines",
    "transmissions",
    "specifications",
    "installed_equipment",
    "optional_equipment",
    "colors",
    "safety_equipment",
    "warranties",
    "epa_fuel_efficiency"
})
public class UsStyle {

    @JsonProperty("name")
    private String name;
    @JsonProperty("vehicle_id")
    private String vehicleId;
    @JsonProperty("complete")
    private String complete;
    @JsonProperty("market")
    private String market;
    @JsonProperty("fleet")
    private String fleet;
    @JsonProperty("basic_data")
    private BasicData_ basicData;
    @JsonProperty("pricing")
    private Pricing_ pricing;
    @JsonProperty("engines")
    private List<Engine_> engines = new ArrayList<Engine_>();
    @JsonProperty("transmissions")
    private List<Transmission_> transmissions = new ArrayList<Transmission_>();
    @JsonProperty("specifications")
    private List<Specification__> specifications = new ArrayList<Specification__>();
    @JsonProperty("installed_equipment")
    private List<InstalledEquipment_> installedEquipment = new ArrayList<InstalledEquipment_>();
    @JsonProperty("optional_equipment")
    private List<OptionalEquipment> optionalEquipment = new ArrayList<OptionalEquipment>();
    @JsonProperty("colors")
    private Colors colors;
    @JsonProperty("safety_equipment")
    private SafetyEquipment safetyEquipment;
    @JsonProperty("warranties")
    private List<Warranty> warranties = new ArrayList<Warranty>();
    @JsonProperty("epa_fuel_efficiency")
    private List<EpaFuelEfficiency> epaFuelEfficiency = new ArrayList<EpaFuelEfficiency>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The vehicleId
     */
    @JsonProperty("vehicle_id")
    public String getVehicleId() {
        return vehicleId;
    }

    /**
     * 
     * @param vehicleId
     *     The vehicle_id
     */
    @JsonProperty("vehicle_id")
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * 
     * @return
     *     The complete
     */
    @JsonProperty("complete")
    public String getComplete() {
        return complete;
    }

    /**
     * 
     * @param complete
     *     The complete
     */
    @JsonProperty("complete")
    public void setComplete(String complete) {
        this.complete = complete;
    }

    /**
     * 
     * @return
     *     The market
     */
    @JsonProperty("market")
    public String getMarket() {
        return market;
    }

    /**
     * 
     * @param market
     *     The market
     */
    @JsonProperty("market")
    public void setMarket(String market) {
        this.market = market;
    }

    /**
     * 
     * @return
     *     The fleet
     */
    @JsonProperty("fleet")
    public String getFleet() {
        return fleet;
    }

    /**
     * 
     * @param fleet
     *     The fleet
     */
    @JsonProperty("fleet")
    public void setFleet(String fleet) {
        this.fleet = fleet;
    }

    /**
     * 
     * @return
     *     The basicData
     */
    @JsonProperty("basic_data")
    public BasicData_ getBasicData() {
        return basicData;
    }

    /**
     * 
     * @param basicData
     *     The basic_data
     */
    @JsonProperty("basic_data")
    public void setBasicData(BasicData_ basicData) {
        this.basicData = basicData;
    }

    /**
     * 
     * @return
     *     The pricing
     */
    @JsonProperty("pricing")
    public Pricing_ getPricing() {
        return pricing;
    }

    /**
     * 
     * @param pricing
     *     The pricing
     */
    @JsonProperty("pricing")
    public void setPricing(Pricing_ pricing) {
        this.pricing = pricing;
    }

    /**
     * 
     * @return
     *     The engines
     */
    @JsonProperty("engines")
    public List<Engine_> getEngines() {
        return engines;
    }

    /**
     * 
     * @param engines
     *     The engines
     */
    @JsonProperty("engines")
    public void setEngines(List<Engine_> engines) {
        this.engines = engines;
    }

    /**
     * 
     * @return
     *     The transmissions
     */
    @JsonProperty("transmissions")
    public List<Transmission_> getTransmissions() {
        return transmissions;
    }

    /**
     * 
     * @param transmissions
     *     The transmissions
     */
    @JsonProperty("transmissions")
    public void setTransmissions(List<Transmission_> transmissions) {
        this.transmissions = transmissions;
    }

    /**
     * 
     * @return
     *     The specifications
     */
    @JsonProperty("specifications")
    public List<Specification__> getSpecifications() {
        return specifications;
    }

    /**
     * 
     * @param specifications
     *     The specifications
     */
    @JsonProperty("specifications")
    public void setSpecifications(List<Specification__> specifications) {
        this.specifications = specifications;
    }

    /**
     * 
     * @return
     *     The installedEquipment
     */
    @JsonProperty("installed_equipment")
    public List<InstalledEquipment_> getInstalledEquipment() {
        return installedEquipment;
    }

    /**
     * 
     * @param installedEquipment
     *     The installed_equipment
     */
    @JsonProperty("installed_equipment")
    public void setInstalledEquipment(List<InstalledEquipment_> installedEquipment) {
        this.installedEquipment = installedEquipment;
    }

    /**
     * 
     * @return
     *     The optionalEquipment
     */
    @JsonProperty("optional_equipment")
    public List<OptionalEquipment> getOptionalEquipment() {
        return optionalEquipment;
    }

    /**
     * 
     * @param optionalEquipment
     *     The optional_equipment
     */
    @JsonProperty("optional_equipment")
    public void setOptionalEquipment(List<OptionalEquipment> optionalEquipment) {
        this.optionalEquipment = optionalEquipment;
    }

    /**
     * 
     * @return
     *     The colors
     */
    @JsonProperty("colors")
    public Colors getColors() {
        return colors;
    }

    /**
     * 
     * @param colors
     *     The colors
     */
    @JsonProperty("colors")
    public void setColors(Colors colors) {
        this.colors = colors;
    }

    /**
     * 
     * @return
     *     The safetyEquipment
     */
    @JsonProperty("safety_equipment")
    public SafetyEquipment getSafetyEquipment() {
        return safetyEquipment;
    }

    /**
     * 
     * @param safetyEquipment
     *     The safety_equipment
     */
    @JsonProperty("safety_equipment")
    public void setSafetyEquipment(SafetyEquipment safetyEquipment) {
        this.safetyEquipment = safetyEquipment;
    }

    /**
     * 
     * @return
     *     The warranties
     */
    @JsonProperty("warranties")
    public List<Warranty> getWarranties() {
        return warranties;
    }

    /**
     * 
     * @param warranties
     *     The warranties
     */
    @JsonProperty("warranties")
    public void setWarranties(List<Warranty> warranties) {
        this.warranties = warranties;
    }

    /**
     * 
     * @return
     *     The epaFuelEfficiency
     */
    @JsonProperty("epa_fuel_efficiency")
    public List<EpaFuelEfficiency> getEpaFuelEfficiency() {
        return epaFuelEfficiency;
    }

    /**
     * 
     * @param epaFuelEfficiency
     *     The epa_fuel_efficiency
     */
    @JsonProperty("epa_fuel_efficiency")
    public void setEpaFuelEfficiency(List<EpaFuelEfficiency> epaFuelEfficiency) {
        this.epaFuelEfficiency = epaFuelEfficiency;
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
