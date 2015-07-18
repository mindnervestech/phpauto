
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
    "service_provider",
    "decoder_version",
    "decoder_errors"
})
public class DecoderMessages {

    @JsonProperty("service_provider")
    private String serviceProvider;
    @JsonProperty("decoder_version")
    private String decoderVersion;
    @JsonProperty("decoder_errors")
    private List<Object> decoderErrors = new ArrayList<Object>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The serviceProvider
     */
    @JsonProperty("service_provider")
    public String getServiceProvider() {
        return serviceProvider;
    }

    /**
     * 
     * @param serviceProvider
     *     The service_provider
     */
    @JsonProperty("service_provider")
    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    /**
     * 
     * @return
     *     The decoderVersion
     */
    @JsonProperty("decoder_version")
    public String getDecoderVersion() {
        return decoderVersion;
    }

    /**
     * 
     * @param decoderVersion
     *     The decoder_version
     */
    @JsonProperty("decoder_version")
    public void setDecoderVersion(String decoderVersion) {
        this.decoderVersion = decoderVersion;
    }

    /**
     * 
     * @return
     *     The decoderErrors
     */
    @JsonProperty("decoder_errors")
    public List<Object> getDecoderErrors() {
        return decoderErrors;
    }

    /**
     * 
     * @param decoderErrors
     *     The decoder_errors
     */
    @JsonProperty("decoder_errors")
    public void setDecoderErrors(List<Object> decoderErrors) {
        this.decoderErrors = decoderErrors;
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
