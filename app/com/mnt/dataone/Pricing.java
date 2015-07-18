
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
    "msrp",
    "invoice_price",
    "destination_charge",
    "gas_guzzler_tax"
})
public class Pricing {

    @JsonProperty("msrp")
    private String msrp;
    @JsonProperty("invoice_price")
    private String invoicePrice;
    @JsonProperty("destination_charge")
    private String destinationCharge;
    @JsonProperty("gas_guzzler_tax")
    private String gasGuzzlerTax;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     *     The destinationCharge
     */
    @JsonProperty("destination_charge")
    public String getDestinationCharge() {
        return destinationCharge;
    }

    /**
     * 
     * @param destinationCharge
     *     The destination_charge
     */
    @JsonProperty("destination_charge")
    public void setDestinationCharge(String destinationCharge) {
        this.destinationCharge = destinationCharge;
    }

    /**
     * 
     * @return
     *     The gasGuzzlerTax
     */
    @JsonProperty("gas_guzzler_tax")
    public String getGasGuzzlerTax() {
        return gasGuzzlerTax;
    }

    /**
     * 
     * @param gasGuzzlerTax
     *     The gas_guzzler_tax
     */
    @JsonProperty("gas_guzzler_tax")
    public void setGasGuzzlerTax(String gasGuzzlerTax) {
        this.gasGuzzlerTax = gasGuzzlerTax;
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
