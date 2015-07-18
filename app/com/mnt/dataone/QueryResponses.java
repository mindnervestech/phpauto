
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
    "Request-Sample"
})
public class QueryResponses {

    @JsonProperty("Request-Sample")
    private com.mnt.dataone.RequestSample RequestSample;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The RequestSample
     */
    @JsonProperty("Request-Sample")
    public com.mnt.dataone.RequestSample getRequestSample() {
        return RequestSample;
    }

    /**
     * 
     * @param RequestSample
     *     The Request-Sample
     */
    @JsonProperty("Request-Sample")
    public void setRequestSample(com.mnt.dataone.RequestSample RequestSample) {
        this.RequestSample = RequestSample;
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
