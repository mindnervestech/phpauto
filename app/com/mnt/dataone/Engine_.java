
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
    "name",
    "brand",
    "marketing_name",
    "engine_id",
    "availability",
    "aspiration",
    "block_type",
    "bore",
    "cam_type",
    "compression",
    "cylinders",
    "displacement",
    "fuel_induction",
    "fuel_quality",
    "fuel_type",
    "msrp",
    "invoice_price",
    "max_hp",
    "max_hp_at",
    "max_payload",
    "max_torque",
    "max_torque_at",
    "oil_capacity",
    "order_code",
    "redline",
    "stroke",
    "valve_timing",
    "valves"
})
public class Engine_ {

    @JsonProperty("name")
    private String name;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("marketing_name")
    private String marketingName;
    @JsonProperty("engine_id")
    private String engineId;
    @JsonProperty("availability")
    private String availability;
    @JsonProperty("aspiration")
    private String aspiration;
    @JsonProperty("block_type")
    private String blockType;
    @JsonProperty("bore")
    private String bore;
    @JsonProperty("cam_type")
    private String camType;
    @JsonProperty("compression")
    private String compression;
    @JsonProperty("cylinders")
    private String cylinders;
    @JsonProperty("displacement")
    private String displacement;
    @JsonProperty("fuel_induction")
    private String fuelInduction;
    @JsonProperty("fuel_quality")
    private String fuelQuality;
    @JsonProperty("fuel_type")
    private String fuelType;
    @JsonProperty("msrp")
    private String msrp;
    @JsonProperty("invoice_price")
    private String invoicePrice;
    @JsonProperty("max_hp")
    private String maxHp;
    @JsonProperty("max_hp_at")
    private String maxHpAt;
    @JsonProperty("max_payload")
    private String maxPayload;
    @JsonProperty("max_torque")
    private String maxTorque;
    @JsonProperty("max_torque_at")
    private String maxTorqueAt;
    @JsonProperty("oil_capacity")
    private String oilCapacity;
    @JsonProperty("order_code")
    private String orderCode;
    @JsonProperty("redline")
    private String redline;
    @JsonProperty("stroke")
    private String stroke;
    @JsonProperty("valve_timing")
    private String valveTiming;
    @JsonProperty("valves")
    private String valves;
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
     *     The brand
     */
    @JsonProperty("brand")
    public String getBrand() {
        return brand;
    }

    /**
     * 
     * @param brand
     *     The brand
     */
    @JsonProperty("brand")
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 
     * @return
     *     The marketingName
     */
    @JsonProperty("marketing_name")
    public String getMarketingName() {
        return marketingName;
    }

    /**
     * 
     * @param marketingName
     *     The marketing_name
     */
    @JsonProperty("marketing_name")
    public void setMarketingName(String marketingName) {
        this.marketingName = marketingName;
    }

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
     *     The availability
     */
    @JsonProperty("availability")
    public String getAvailability() {
        return availability;
    }

    /**
     * 
     * @param availability
     *     The availability
     */
    @JsonProperty("availability")
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    /**
     * 
     * @return
     *     The aspiration
     */
    @JsonProperty("aspiration")
    public String getAspiration() {
        return aspiration;
    }

    /**
     * 
     * @param aspiration
     *     The aspiration
     */
    @JsonProperty("aspiration")
    public void setAspiration(String aspiration) {
        this.aspiration = aspiration;
    }

    /**
     * 
     * @return
     *     The blockType
     */
    @JsonProperty("block_type")
    public String getBlockType() {
        return blockType;
    }

    /**
     * 
     * @param blockType
     *     The block_type
     */
    @JsonProperty("block_type")
    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    /**
     * 
     * @return
     *     The bore
     */
    @JsonProperty("bore")
    public String getBore() {
        return bore;
    }

    /**
     * 
     * @param bore
     *     The bore
     */
    @JsonProperty("bore")
    public void setBore(String bore) {
        this.bore = bore;
    }

    /**
     * 
     * @return
     *     The camType
     */
    @JsonProperty("cam_type")
    public String getCamType() {
        return camType;
    }

    /**
     * 
     * @param camType
     *     The cam_type
     */
    @JsonProperty("cam_type")
    public void setCamType(String camType) {
        this.camType = camType;
    }

    /**
     * 
     * @return
     *     The compression
     */
    @JsonProperty("compression")
    public String getCompression() {
        return compression;
    }

    /**
     * 
     * @param compression
     *     The compression
     */
    @JsonProperty("compression")
    public void setCompression(String compression) {
        this.compression = compression;
    }

    /**
     * 
     * @return
     *     The cylinders
     */
    @JsonProperty("cylinders")
    public String getCylinders() {
        return cylinders;
    }

    /**
     * 
     * @param cylinders
     *     The cylinders
     */
    @JsonProperty("cylinders")
    public void setCylinders(String cylinders) {
        this.cylinders = cylinders;
    }

    /**
     * 
     * @return
     *     The displacement
     */
    @JsonProperty("displacement")
    public String getDisplacement() {
        return displacement;
    }

    /**
     * 
     * @param displacement
     *     The displacement
     */
    @JsonProperty("displacement")
    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    /**
     * 
     * @return
     *     The fuelInduction
     */
    @JsonProperty("fuel_induction")
    public String getFuelInduction() {
        return fuelInduction;
    }

    /**
     * 
     * @param fuelInduction
     *     The fuel_induction
     */
    @JsonProperty("fuel_induction")
    public void setFuelInduction(String fuelInduction) {
        this.fuelInduction = fuelInduction;
    }

    /**
     * 
     * @return
     *     The fuelQuality
     */
    @JsonProperty("fuel_quality")
    public String getFuelQuality() {
        return fuelQuality;
    }

    /**
     * 
     * @param fuelQuality
     *     The fuel_quality
     */
    @JsonProperty("fuel_quality")
    public void setFuelQuality(String fuelQuality) {
        this.fuelQuality = fuelQuality;
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
     *     The msrp
     */
    @JsonProperty("msrp")
    public String getMsrp() {
        return msrp;
    }

    /**
     * 
     * @param msrp
     *     The msrp
     */
    @JsonProperty("msrp")
    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    /**
     * 
     * @return
     *     The invoicePrice
     */
    @JsonProperty("invoice_price")
    public String getInvoicePrice() {
        return invoicePrice;
    }

    /**
     * 
     * @param invoicePrice
     *     The invoice_price
     */
    @JsonProperty("invoice_price")
    public void setInvoicePrice(String invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    /**
     * 
     * @return
     *     The maxHp
     */
    @JsonProperty("max_hp")
    public String getMaxHp() {
        return maxHp;
    }

    /**
     * 
     * @param maxHp
     *     The max_hp
     */
    @JsonProperty("max_hp")
    public void setMaxHp(String maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * 
     * @return
     *     The maxHpAt
     */
    @JsonProperty("max_hp_at")
    public String getMaxHpAt() {
        return maxHpAt;
    }

    /**
     * 
     * @param maxHpAt
     *     The max_hp_at
     */
    @JsonProperty("max_hp_at")
    public void setMaxHpAt(String maxHpAt) {
        this.maxHpAt = maxHpAt;
    }

    /**
     * 
     * @return
     *     The maxPayload
     */
    @JsonProperty("max_payload")
    public String getMaxPayload() {
        return maxPayload;
    }

    /**
     * 
     * @param maxPayload
     *     The max_payload
     */
    @JsonProperty("max_payload")
    public void setMaxPayload(String maxPayload) {
        this.maxPayload = maxPayload;
    }

    /**
     * 
     * @return
     *     The maxTorque
     */
    @JsonProperty("max_torque")
    public String getMaxTorque() {
        return maxTorque;
    }

    /**
     * 
     * @param maxTorque
     *     The max_torque
     */
    @JsonProperty("max_torque")
    public void setMaxTorque(String maxTorque) {
        this.maxTorque = maxTorque;
    }

    /**
     * 
     * @return
     *     The maxTorqueAt
     */
    @JsonProperty("max_torque_at")
    public String getMaxTorqueAt() {
        return maxTorqueAt;
    }

    /**
     * 
     * @param maxTorqueAt
     *     The max_torque_at
     */
    @JsonProperty("max_torque_at")
    public void setMaxTorqueAt(String maxTorqueAt) {
        this.maxTorqueAt = maxTorqueAt;
    }

    /**
     * 
     * @return
     *     The oilCapacity
     */
    @JsonProperty("oil_capacity")
    public String getOilCapacity() {
        return oilCapacity;
    }

    /**
     * 
     * @param oilCapacity
     *     The oil_capacity
     */
    @JsonProperty("oil_capacity")
    public void setOilCapacity(String oilCapacity) {
        this.oilCapacity = oilCapacity;
    }

    /**
     * 
     * @return
     *     The orderCode
     */
    @JsonProperty("order_code")
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 
     * @param orderCode
     *     The order_code
     */
    @JsonProperty("order_code")
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * 
     * @return
     *     The redline
     */
    @JsonProperty("redline")
    public String getRedline() {
        return redline;
    }

    /**
     * 
     * @param redline
     *     The redline
     */
    @JsonProperty("redline")
    public void setRedline(String redline) {
        this.redline = redline;
    }

    /**
     * 
     * @return
     *     The stroke
     */
    @JsonProperty("stroke")
    public String getStroke() {
        return stroke;
    }

    /**
     * 
     * @param stroke
     *     The stroke
     */
    @JsonProperty("stroke")
    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    /**
     * 
     * @return
     *     The valveTiming
     */
    @JsonProperty("valve_timing")
    public String getValveTiming() {
        return valveTiming;
    }

    /**
     * 
     * @param valveTiming
     *     The valve_timing
     */
    @JsonProperty("valve_timing")
    public void setValveTiming(String valveTiming) {
        this.valveTiming = valveTiming;
    }

    /**
     * 
     * @return
     *     The valves
     */
    @JsonProperty("valves")
    public String getValves() {
        return valves;
    }

    /**
     * 
     * @param valves
     *     The valves
     */
    @JsonProperty("valves")
    public void setValves(String valves) {
        this.valves = valves;
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
