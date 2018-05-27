package com.mc851.xcommerce.jobs

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class Jobs {

    @Scheduled(fixedRate = 60000)
    fun preShipmentJob() {
        TODO()
    }

    @Scheduled(fixedRate = 60000)
    fun releaseJob() {
        TODO()
    }

}