
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
    "common_us_data",
    "us_styles"
})
public class UsMarketData {

    @JsonProperty("common_us_data")
    private CommonUsData commonUsData;
    @JsonProperty("us_styles")
    private List<UsStyle> usStyles = new ArrayList<UsStyle>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The commonUsData
     */
    @JsonProperty("common_us_data")
    public CommonUsData getCommonUsData() {
        return commonUsData;
    }

    /**
     * 
     * @param commonUsData
     *     The common_us_data
     */
    @JsonProperty("common_us_data")
    public void setCommonUsData(CommonUsData commonUsData) {
        this.commonUsData = commonUsData;
    }

    /**
     * 
     * @return
     *     The usStyles
     */
    @JsonProperty("us_styles")
    public List<UsStyle> getUsStyles() {
        return usStyles;
    }

    /**
     * 
     * @param usStyles
     *     The us_styles
     */
    @JsonProperty("us_styles")
    public void setUsStyles(List<UsStyle> usStyles) {
        this.usStyles = usStyles;
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
