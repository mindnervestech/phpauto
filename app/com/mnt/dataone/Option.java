
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
    "option_id",
    "order_code",
    "installed",
    "fleet",
    "install_type",
    "invoice_price",
    "msrp",
    "description"
})
public class Option {

    @JsonProperty("name")
    private String name;
    @JsonProperty("option_id")
    private String optionId;
    @JsonProperty("order_code")
    private String orderCode;
    @JsonProperty("installed")
    private String installed;
    @JsonProperty("fleet")
    private String fleet;
    @JsonProperty("install_type")
    private String installType;
    @JsonProperty("invoice_price")
    private String invoicePrice;
    @JsonProperty("msrp")
    private String msrp;
    @JsonProperty("description")
    private String description;
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
     *     The optionId
     */
    @JsonProperty("option_id")
    public String getOptionId() {
        return optionId;
    }

    /**
     * 
     * @param optionId
     *     The option_id
     */
    @JsonProperty("option_id")
    public void setOptionId(String optionId) {
        this.optionId = optionId;
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
     *     The installed
     */
    @JsonProperty("installed")
    public String getInstalled() {
        return installed;
    }

    /**
     * 
     * @param installed
     *     The installed
     */
    @JsonProperty("installed")
    public void setInstalled(String installed) {
        this.installed = installed;
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
     *     The installType
     */
    @JsonProperty("install_type")
    public String getInstallType() {
        return installType;
    }

    /**
     * 
     * @param installType
     *     The install_type
     */
    @JsonProperty("install_type")
    public void setInstallType(String installType) {
        this.installType = installType;
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
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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
