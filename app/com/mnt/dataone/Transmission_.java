
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
    "transmission_id",
    "availability",
    "type",
    "detail_type",
    "gears",
    "msrp",
    "invoice_price",
    "order_code"
})
public class Transmission_ {

    @JsonProperty("name")
    private String name;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("marketing_name")
    private String marketingName;
    @JsonProperty("transmission_id")
    private String transmissionId;
    @JsonProperty("availability")
    private String availability;
    @JsonProperty("type")
    private String type;
    @JsonProperty("detail_type")
    private String detailType;
    @JsonProperty("gears")
    private String gears;
    @JsonProperty("msrp")
    private String msrp;
    @JsonProperty("invoice_price")
    private String invoicePrice;
    @JsonProperty("order_code")
    private String orderCode;
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
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The detailType
     */
    @JsonProperty("detail_type")
    public String getDetailType() {
        return detailType;
    }

    /**
     * 
     * @param detailType
     *     The detail_type
     */
    @JsonProperty("detail_type")
    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    /**
     * 
     * @return
     *     The gears
     */
    @JsonProperty("gears")
    public String getGears() {
        return gears;
    }

    /**
     * 
     * @param gears
     *     The gears
     */
    @JsonProperty("gears")
    public void setGears(String gears) {
        this.gears = gears;
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
