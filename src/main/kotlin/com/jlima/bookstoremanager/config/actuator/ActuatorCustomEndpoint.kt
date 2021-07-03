package com.jlima.bookstoremanager.config.actuator

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Component

@Component
@EndpointWebExtension(endpoint = InfoEndpoint::class)
class ActuatorCustomEndpoint(
    private val infoEndpoint: InfoEndpoint,
    private val buildProperties: BuildProperties
) {
    @ReadOperation
    fun info(): WebEndpointResponse<Map<String, Any>> {
        val info: Map<String, Any> = this.infoEndpoint.info()
        val customInfo = HashMap<String, Any>(info)
        customInfo["actuatorConfig"] = "custom configuration from actuator config file"
        return WebEndpointResponse(customInfo, 200)
    }
}
