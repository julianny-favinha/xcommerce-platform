package com.mc920.xcommerce.conf

import com.mc920.xcommerce.clients.ProductClient
import com.mc920.xcommerce.clients.product01.ProductClientOkHttp
import com.mc920.xcommerce.dao.ProductDao
import com.mc920.xcommerce.dao.ProductDaoPostgres
import com.mc920.xcommerce.service.ProductService
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
@PropertySource("classpath:application.properties")
class BeanConfiguration {

    @Autowired
    lateinit var environment: Environment

    // DataSource
    @Bean
    fun datasource(): HikariDataSource {
        val dataSource = HikariDataSource()

        dataSource.driverClassName = "org.postgresql.Driver"
        dataSource.jdbcUrl = environment.getProperty("spring.datasource.url")
        dataSource.username = environment.getProperty("spring.datasource.username")
        dataSource.password = environment.getProperty("spring.datasource.password")

        return dataSource
    }

    @Bean
    fun jdbcTemplate(datasource: DataSource): JdbcTemplate {
        return JdbcTemplate(datasource)
    }

    // DAO
    @Bean
    fun productDao(jdbcTemplate: JdbcTemplate): ProductDao {
        return ProductDaoPostgres(jdbcTemplate)
    }

    // Client
    @Bean
    fun productClient(): ProductClient {
        return ProductClientOkHttp()
    }

    // Service
    @Bean
    fun productService(productClient: ProductClient, productDao: ProductDao): ProductService {
        return ProductService(productClient, productDao)
    }
}
