
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
    "market",
    "year",
    "make",
    "model",
    "trim",
    "vehicle_type",
    "body_type",
    "body_subtype",
    "oem_body_style",
    "doors",
    "oem_doors",
    "model_number",
    "package_code",
    "drive_type",
    "brake_system",
    "restraint_type",
    "country_of_manufacture",
    "plant"
})
public class BasicData_ {

    @JsonProperty("market")
    private String market;
    @JsonProperty("year")
    private String year;
    @JsonProperty("make")
    private String make;
    @JsonProperty("model")
    private String model;
    @JsonProperty("trim")
    private String trim;
    @JsonProperty("vehicle_type")
    private String vehicleType;
    @JsonProperty("body_type")
    private String bodyType;
    @JsonProperty("body_subtype")
    private String bodySubtype;
    @JsonProperty("oem_body_style")
    private String oemBodyStyle;
    @JsonProperty("doors")
    private String doors;
    @JsonProperty("oem_doors")
    private String oemDoors;
    @JsonProperty("model_number")
    private String modelNumber;
    @JsonProperty("package_code")
    private String packageCode;
    @JsonProperty("drive_type")
    private String driveType;
    @JsonProperty("brake_system")
    private String brakeSystem;
    @JsonProperty("restraint_type")
    private String restraintType;
    @JsonProperty("country_of_manufacture")
    private String countryOfManufacture;
    @JsonProperty("plant")
    private String plant;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     *     The year
     */
    @JsonProperty("year")
    public String getYear() {
        return year;
    }

    /**
     * 
     * @param year
     *     The year
     */
    @JsonProperty("year")
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * 
     * @return
     *     The make
     */
    @JsonProperty("make")
    public String getMake() {
        return make;
    }

    /**
     * 
     * @param make
     *     The make
     */
    @JsonProperty("make")
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * 
     * @return
     *     The model
     */
    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    /**
     * 
     * @param model
     *     The model
     */
    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 
     * @return
     *     The trim
     */
    @JsonProperty("trim")
    public String getTrim() {
        return trim;
    }

    /**
     * 
     * @param trim
     *     The trim
     */
    @JsonProperty("trim")
    public void setTrim(String trim) {
        this.trim = trim;
    }

    /**
     * 
     * @return
     *     The vehicleType
     */
    @JsonProperty("vehicle_type")
    public String getVehicleType() {
        return vehicleType;
    }

    /**
     * 
     * @param vehicleType
     *     The vehicle_type
     */
    @JsonProperty("vehicle_type")
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    /**
     * 
     * @return
     *     The bodyType
     */
    @JsonProperty("body_type")
    public String getBodyType() {
        return bodyType;
    }

    /**
     * 
     * @param bodyType
     *     The body_type
     */
    @JsonProperty("body_type")
    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    /**
     * 
     * @return
     *     The bodySubtype
     */
    @JsonProperty("body_subtype")
    public String getBodySubtype() {
        return bodySubtype;
    }

    /**
     * 
     * @param bodySubtype
     *     The body_subtype
     */
    @JsonProperty("body_subtype")
    public void setBodySubtype(String bodySubtype) {
        this.bodySubtype = bodySubtype;
    }

    /**
     * 
     * @return
     *     The oemBodyStyle
     */
    @JsonProperty("oem_body_style")
    public String getOemBodyStyle() {
        return oemBodyStyle;
    }

    /**
     * 
     * @param oemBodyStyle
     *     The oem_body_style
     */
    @JsonProperty("oem_body_style")
    public void setOemBodyStyle(String oemBodyStyle) {
        this.oemBodyStyle = oemBodyStyle;
    }

    /**
     * 
     * @return
     *     The doors
     */
    @JsonProperty("doors")
    public String getDoors() {
        return doors;
    }

    /**
     * 
     * @param doors
     *     The doors
     */
    @JsonProperty("doors")
    public void setDoors(String doors) {
        this.doors = doors;
    }

    /**
     * 
     * @return
     *     The oemDoors
     */
    @JsonProperty("oem_doors")
    public String getOemDoors() {
        return oemDoors;
    }

    /**
     * 
     * @param oemDoors
     *     The oem_doors
     */
    @JsonProperty("oem_doors")
    public void setOemDoors(String oemDoors) {
        this.oemDoors = oemDoors;
    }

    /**
     * 
     * @return
     *     The modelNumber
     */
    @JsonProperty("model_number")
    public String getModelNumber() {
        return modelNumber;
    }

    /**
     * 
     * @param modelNumber
     *     The model_number
     */
    @JsonProperty("model_number")
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    /**
     * 
     * @return
     *     The packageCode
     */
    @JsonProperty("package_code")
    public String getPackageCode() {
        return packageCode;
    }

    /**
     * 
     * @param packageCode
     *     The package_code
     */
    @JsonProperty("package_code")
    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    /**
     * 
     * @return
     *     The driveType
     */
    @JsonProperty("drive_type")
    public String getDriveType() {
        return driveType;
    }

    /**
     * 
     * @param driveType
     *     The drive_type
     */
    @JsonProperty("drive_type")
    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    /**
     * 
     * @return
     *     The brakeSystem
     */
    @JsonProperty("brake_system")
    public String getBrakeSystem() {
        return brakeSystem;
    }

    /**
     * 
     * @param brakeSystem
     *     The brake_system
     */
    @JsonProperty("brake_system")
    public void setBrakeSystem(String brakeSystem) {
        this.brakeSystem = brakeSystem;
    }

    /**
     * 
     * @return
     *     The restraintType
     */
    @JsonProperty("restraint_type")
    public String getRestraintType() {
        return restraintType;
    }

    /**
     * 
     * @param restraintType
     *     The restraint_type
     */
    @JsonProperty("restraint_type")
    public void setRestraintType(String restraintType) {
        this.restraintType = restraintType;
    }

    /**
     * 
     * @return
     *     The countryOfManufacture
     */
    @JsonProperty("country_of_manufacture")
    public String getCountryOfManufacture() {
        return countryOfManufacture;
    }

    /**
     * 
     * @param countryOfManufacture
     *     The country_of_manufacture
     */
    @JsonProperty("country_of_manufacture")
    public void setCountryOfManufacture(String countryOfManufacture) {
        this.countryOfManufacture = countryOfManufacture;
    }

    /**
     * 
     * @return
     *     The plant
     */
    @JsonProperty("plant")
    public String getPlant() {
        return plant;
    }

    /**
     * 
     * @param plant
     *     The plant
     */
    @JsonProperty("plant")
    public void setPlant(String plant) {
        this.plant = plant;
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
