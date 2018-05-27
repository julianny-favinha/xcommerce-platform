package com.mc851.xcommerce.jobs

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class Jobs {

    @Scheduled(fixedRate = 60000)
    fun preShipmentJob() {
        System.out.println("Time in task: " + (System.currentTimeMillis() / 1000))
    }

    @Scheduled(fixedRate = 60000)
    fun releaseJob() {
        System.out.println("Time in task: " + (System.currentTimeMillis() / 1000))
    }

}