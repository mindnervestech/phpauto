
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
    "decoder_messages",
    "query_responses"
})
public class ResponseData {

    @JsonProperty("decoder_messages")
    private DecoderMessages decoderMessages;
    @JsonProperty("query_responses")
    private QueryResponses queryResponses;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The decoderMessages
     */
    @JsonProperty("decoder_messages")
    public DecoderMessages getDecoderMessages() {
        return decoderMessages;
    }

    /**
     * 
     * @param decoderMessages
     *     The decoder_messages
     */
    @JsonProperty("decoder_messages")
    public void setDecoderMessages(DecoderMessages decoderMessages) {
        this.decoderMessages = decoderMessages;
    }

    /**
     * 
     * @return
     *     The queryResponses
     */
    @JsonProperty("query_responses")
    public QueryResponses getQueryResponses() {
        return queryResponses;
    }

    /**
     * 
     * @param queryResponses
     *     The query_responses
     */
    @JsonProperty("query_responses")
    public void setQueryResponses(QueryResponses queryResponses) {
        this.queryResponses = queryResponses;
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
