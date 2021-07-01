package com.jlima.bookstoremanager.config.actuator

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.stereotype.Component

@Component
@EndpointWebExtension(endpoint = InfoEndpoint::class)
class ActuatorCustomEndpoint(
    private val infoEndpoint: InfoEndpoint
) {
    @ReadOperation
    fun info(): WebEndpointResponse<Map<String, Any>> {
        val info: Map<String, Any> = this.infoEndpoint.info()
        val status = getStatus(info)
        val customInfo = HashMap<String, Any>(info)
        customInfo["customInfo"] = "customValue"
        return WebEndpointResponse(customInfo, status)
    }

    private fun getStatus(status: Map<String, Any>): Int {
        return 200
    }
}