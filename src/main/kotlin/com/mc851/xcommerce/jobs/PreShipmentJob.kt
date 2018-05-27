package com.mc851.xcommerce.jobs

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class PreShipmentJob {

    @Scheduled(fixedRate = 1000)
    fun execute() {
        System.out.println("Fixed rate task - " + System.currentTimeMillis() / 1000)
    }

}