
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
    "abs_two_wheel",
    "abs_four_wheel",
    "airbags_front_driver",
    "airbags_front_passenger",
    "airbags_side_impact",
    "airbags_side_curtain",
    "brake_assist",
    "daytime_running_lights",
    "electronic_stability_control",
    "electronic_traction_control",
    "tire_pressure_monitoring_system",
    "rollover_stability_control"
})
public class SafetyEquipment {

    @JsonProperty("abs_two_wheel")
    private String absTwoWheel;
    @JsonProperty("abs_four_wheel")
    private String absFourWheel;
    @JsonProperty("airbags_front_driver")
    private String airbagsFrontDriver;
    @JsonProperty("airbags_front_passenger")
    private String airbagsFrontPassenger;
    @JsonProperty("airbags_side_impact")
    private String airbagsSideImpact;
    @JsonProperty("airbags_side_curtain")
    private String airbagsSideCurtain;
    @JsonProperty("brake_assist")
    private String brakeAssist;
    @JsonProperty("daytime_running_lights")
    private String daytimeRunningLights;
    @JsonProperty("electronic_stability_control")
    private String electronicStabilityControl;
    @JsonProperty("electronic_traction_control")
    private String electronicTractionControl;
    @JsonProperty("tire_pressure_monitoring_system")
    private String tirePressureMonitoringSystem;
    @JsonProperty("rollover_stability_control")
    private String rolloverStabilityControl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The absTwoWheel
     */
    @JsonProperty("abs_two_wheel")
    public String getAbsTwoWheel() {
        return absTwoWheel;
    }

    /**
     * 
     * @param absTwoWheel
     *     The abs_two_wheel
     */
    @JsonProperty("abs_two_wheel")
    public void setAbsTwoWheel(String absTwoWheel) {
        this.absTwoWheel = absTwoWheel;
    }

    /**
     * 
     * @return
     *     The absFourWheel
     */
    @JsonProperty("abs_four_wheel")
    public String getAbsFourWheel() {
        return absFourWheel;
    }

    /**
     * 
     * @param absFourWheel
     *     The abs_four_wheel
     */
    @JsonProperty("abs_four_wheel")
    public void setAbsFourWheel(String absFourWheel) {
        this.absFourWheel = absFourWheel;
    }

    /**
     * 
     * @return
     *     The airbagsFrontDriver
     */
    @JsonProperty("airbags_front_driver")
    public String getAirbagsFrontDriver() {
        return airbagsFrontDriver;
    }

    /**
     * 
     * @param airbagsFrontDriver
     *     The airbags_front_driver
     */
    @JsonProperty("airbags_front_driver")
    public void setAirbagsFrontDriver(String airbagsFrontDriver) {
        this.airbagsFrontDriver = airbagsFrontDriver;
    }

    /**
     * 
     * @return
     *     The airbagsFrontPassenger
     */
    @JsonProperty("airbags_front_passenger")
    public String getAirbagsFrontPassenger() {
        return airbagsFrontPassenger;
    }

    /**
     * 
     * @param airbagsFrontPassenger
     *     The airbags_front_passenger
     */
    @JsonProperty("airbags_front_passenger")
    public void setAirbagsFrontPassenger(String airbagsFrontPassenger) {
        this.airbagsFrontPassenger = airbagsFrontPassenger;
    }

    /**
     * 
     * @return
     *     The airbagsSideImpact
     */
    @JsonProperty("airbags_side_impact")
    public String getAirbagsSideImpact() {
        return airbagsSideImpact;
    }

    /**
     * 
     * @param airbagsSideImpact
     *     The airbags_side_impact
     */
    @JsonProperty("airbags_side_impact")
    public void setAirbagsSideImpact(String airbagsSideImpact) {
        this.airbagsSideImpact = airbagsSideImpact;
    }

    /**
     * 
     * @return
     *     The airbagsSideCurtain
     */
    @JsonProperty("airbags_side_curtain")
    public String getAirbagsSideCurtain() {
        return airbagsSideCurtain;
    }

    /**
     * 
     * @param airbagsSideCurtain
     *     The airbags_side_curtain
     */
    @JsonProperty("airbags_side_curtain")
    public void setAirbagsSideCurtain(String airbagsSideCurtain) {
        this.airbagsSideCurtain = airbagsSideCurtain;
    }

    /**
     * 
     * @return
     *     The brakeAssist
     */
    @JsonProperty("brake_assist")
    public String getBrakeAssist() {
        return brakeAssist;
    }

    /**
     * 
     * @param brakeAssist
     *     The brake_assist
     */
    @JsonProperty("brake_assist")
    public void setBrakeAssist(String brakeAssist) {
        this.brakeAssist = brakeAssist;
    }

    /**
     * 
     * @return
     *     The daytimeRunningLights
     */
    @JsonProperty("daytime_running_lights")
    public String getDaytimeRunningLights() {
        return daytimeRunningLights;
    }

    /**
     * 
     * @param daytimeRunningLights
     *     The daytime_running_lights
     */
    @JsonProperty("daytime_running_lights")
    public void setDaytimeRunningLights(String daytimeRunningLights) {
        this.daytimeRunningLights = daytimeRunningLights;
    }

    /**
     * 
     * @return
     *     The electronicStabilityControl
     */
    @JsonProperty("electronic_stability_control")
    public String getElectronicStabilityControl() {
        return electronicStabilityControl;
    }

    /**
     * 
     * @param electronicStabilityControl
     *     The electronic_stability_control
     */
    @JsonProperty("electronic_stability_control")
    public void setElectronicStabilityControl(String electronicStabilityControl) {
        this.electronicStabilityControl = electronicStabilityControl;
    }

    /**
     * 
     * @return
     *     The electronicTractionControl
     */
    @JsonProperty("electronic_traction_control")
    public String getElectronicTractionControl() {
        return electronicTractionControl;
    }

    /**
     * 
     * @param electronicTractionControl
     *     The electronic_traction_control
     */
    @JsonProperty("electronic_traction_control")
    public void setElectronicTractionControl(String electronicTractionControl) {
        this.electronicTractionControl = electronicTractionControl;
    }

    /**
     * 
     * @return
     *     The tirePressureMonitoringSystem
     */
    @JsonProperty("tire_pressure_monitoring_system")
    public String getTirePressureMonitoringSystem() {
        return tirePressureMonitoringSystem;
    }

    /**
     * 
     * @param tirePressureMonitoringSystem
     *     The tire_pressure_monitoring_system
     */
    @JsonProperty("tire_pressure_monitoring_system")
    public void setTirePressureMonitoringSystem(String tirePressureMonitoringSystem) {
        this.tirePressureMonitoringSystem = tirePressureMonitoringSystem;
    }

    /**
     * 
     * @return
     *     The rolloverStabilityControl
     */
    @JsonProperty("rollover_stability_control")
    public String getRolloverStabilityControl() {
        return rolloverStabilityControl;
    }

    /**
     * 
     * @param rolloverStabilityControl
     *     The rollover_stability_control
     */
    @JsonProperty("rollover_stability_control")
    public void setRolloverStabilityControl(String rolloverStabilityControl) {
        this.rolloverStabilityControl = rolloverStabilityControl;
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
