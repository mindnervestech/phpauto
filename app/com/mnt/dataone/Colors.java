
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
    "exterior_colors",
    "interior_colors",
    "roof_colors"
})
public class Colors {

    @JsonProperty("exterior_colors")
    private List<ExteriorColor> exteriorColors = new ArrayList<ExteriorColor>();
    @JsonProperty("interior_colors")
    private List<InteriorColor> interiorColors = new ArrayList<InteriorColor>();
    @JsonProperty("roof_colors")
    private List<Object> roofColors = new ArrayList<Object>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The exteriorColors
     */
    @JsonProperty("exterior_colors")
    public List<ExteriorColor> getExteriorColors() {
        return exteriorColors;
    }

    /**
     * 
     * @param exteriorColors
     *     The exterior_colors
     */
    @JsonProperty("exterior_colors")
    public void setExteriorColors(List<ExteriorColor> exteriorColors) {
        this.exteriorColors = exteriorColors;
    }

    /**
     * 
     * @return
     *     The interiorColors
     */
    @JsonProperty("interior_colors")
    public List<InteriorColor> getInteriorColors() {
        return interiorColors;
    }

    /**
     * 
     * @param interiorColors
     *     The interior_colors
     */
    @JsonProperty("interior_colors")
    public void setInteriorColors(List<InteriorColor> interiorColors) {
        this.interiorColors = interiorColors;
    }

    /**
     * 
     * @return
     *     The roofColors
     */
    @JsonProperty("roof_colors")
    public List<Object> getRoofColors() {
        return roofColors;
    }

    /**
     * 
     * @param roofColors
     *     The roof_colors
     */
    @JsonProperty("roof_colors")
    public void setRoofColors(List<Object> roofColors) {
        this.roofColors = roofColors;
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
