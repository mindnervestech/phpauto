
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
    "transaction_id",
    "query_error",
    "us_market_data",
    "supplemental_data"
})
public class RequestSample {

    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("query_error")
    private QueryError queryError;
    @JsonProperty("us_market_data")
    private UsMarketData usMarketData;
    @JsonProperty("supplemental_data")
    private SupplementalData supplementalData;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The transactionId
     */
    @JsonProperty("transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * 
     * @param transactionId
     *     The transaction_id
     */
    @JsonProperty("transaction_id")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * 
     * @return
     *     The queryError
     */
    @JsonProperty("query_error")
    public QueryError getQueryError() {
        return queryError;
    }

    /**
     * 
     * @param queryError
     *     The query_error
     */
    @JsonProperty("query_error")
    public void setQueryError(QueryError queryError) {
        this.queryError = queryError;
    }

    /**
     * 
     * @return
     *     The usMarketData
     */
    @JsonProperty("us_market_data")
    public UsMarketData getUsMarketData() {
        return usMarketData;
    }

    /**
     * 
     * @param usMarketData
     *     The us_market_data
     */
    @JsonProperty("us_market_data")
    public void setUsMarketData(UsMarketData usMarketData) {
        this.usMarketData = usMarketData;
    }

    /**
     * 
     * @return
     *     The supplementalData
     */
    @JsonProperty("supplemental_data")
    public SupplementalData getSupplementalData() {
        return supplementalData;
    }

    /**
     * 
     * @param supplementalData
     *     The supplemental_data
     */
    @JsonProperty("supplemental_data")
    public void setSupplementalData(SupplementalData supplementalData) {
        this.supplementalData = supplementalData;
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
