package com.mc851.xcommerce.conf

import com.mc851.xcommerce.clients.ClientesClient
import com.mc851.xcommerce.clients.ProductClient
import com.mc851.xcommerce.clients.clientes01.ClientesClientOkHttp
import com.mc851.xcommerce.clients.product01.ProductClientOkHttp
import com.mc851.xcommerce.dao.category.CategoryDao
import com.mc851.xcommerce.dao.category.CategoryDaoPostgres
import com.mc851.xcommerce.dao.clientes.ClientesDao
import com.mc851.xcommerce.dao.clientes.ClientesDaoPostgres
import com.mc851.xcommerce.dao.product.ProductDao
import com.mc851.xcommerce.dao.product.ProductDaoPostgres
import com.mc851.xcommerce.service.CategoryService
import com.mc851.xcommerce.service.ClientesService
import com.mc851.xcommerce.service.ProductService
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
    fun categoryDao(jdbcTemplate: JdbcTemplate): CategoryDao {
        return CategoryDaoPostgres(jdbcTemplate)
    }

    @Bean
    fun productDao(jdbcTemplate: JdbcTemplate): ProductDao {
        return ProductDaoPostgres(jdbcTemplate)
    }

    @Bean
    fun clientesDao(jdbcTemplate: JdbcTemplate): ClientesDao {
        return ClientesDaoPostgres(jdbcTemplate)
    }

    // Client
    @Bean
    fun productClient(): ProductClient {
        return ProductClientOkHttp()
    }

    @Bean
    fun clientesClient(): ClientesClient {
        return ClientesClientOkHttp()
    }

    // Service
    @Bean
    fun categoryService(productClient: ProductClient, categoryDao: CategoryDao): CategoryService {
        return CategoryService(productClient, categoryDao)
    }

    @Bean
    fun productService(productClient: ProductClient,
                       productDao: ProductDao,
                       categoryService: CategoryService): ProductService {
        return ProductService(productClient, productDao, categoryService)
    }

    @Bean
    fun clientesService(clientesClient: ClientesClient, clientesDao: ClientesDao): ClientesService {
        return ClientesService(clientesClient, clientesDao)
    }
}
