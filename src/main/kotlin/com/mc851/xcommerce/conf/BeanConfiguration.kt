package com.mc851.xcommerce.conf

import com.mc851.xcommerce.clients.CreditClient
import com.mc851.xcommerce.clients.LogisticClient
import com.mc851.xcommerce.clients.UserClient
import com.mc851.xcommerce.clients.credit.CreditClientOkHttp
import com.mc851.xcommerce.clients.logistic.LogisticClientOkHttp
import com.mc851.xcommerce.clients.payment.PaymentClient
import com.mc851.xcommerce.clients.payment.PaymentClientOkHttp
import com.mc851.xcommerce.clients.product01.ProductClient
import com.mc851.xcommerce.clients.product01.ProductClientOkHttp
import com.mc851.xcommerce.clients.user01.UserClientOkHttp
import com.mc851.xcommerce.dao.category.CategoryDao
import com.mc851.xcommerce.dao.category.CategoryDaoPostgres
import com.mc851.xcommerce.dao.logistic.LogisticDao
import com.mc851.xcommerce.dao.logistic.LogisticDaoPostgres
import com.mc851.xcommerce.dao.product.ProductDao
import com.mc851.xcommerce.dao.product.ProductDaoPostgres
import com.mc851.xcommerce.dao.user.UserDao
import com.mc851.xcommerce.dao.user.UserDaoPostgres
import com.mc851.xcommerce.service.CategoryService
import com.mc851.xcommerce.service.CreditService
import com.mc851.xcommerce.service.LogisticService
import com.mc851.xcommerce.service.PaymentService
import com.mc851.xcommerce.service.ProductService
import com.mc851.xcommerce.service.UserService
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
    fun userDao(jdbcTemplate: JdbcTemplate): UserDao {
        return UserDaoPostgres(jdbcTemplate)
    }

    @Bean
    fun logisticDao(jdbcTemplate: JdbcTemplate): LogisticDao {
        return LogisticDaoPostgres(jdbcTemplate)
    }

    // Client
    @Bean
    fun productClient(): ProductClient {
        return ProductClientOkHttp()
    }

    @Bean
    fun paymentClient(): PaymentClient {
        return PaymentClientOkHttp()
    }

    @Bean
    fun userClient(): UserClient {
        return UserClientOkHttp()
    }

    @Bean
    fun creditClient(): CreditClient {
        return CreditClientOkHttp()
    }

    @Bean
    fun logisticClient(): LogisticClient {
        return LogisticClientOkHttp()
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
    fun paymentService(paymentClient: PaymentClient): PaymentService {
        return PaymentService(paymentClient)
    }

    @Bean
    fun userService(userClient: UserClient, userDao: UserDao): UserService {
        return UserService(userClient, userDao)
    }

    @Bean
    fun creditService(creditClient: CreditClient): CreditService {
        return CreditService(creditClient)
    }

    @Bean
    fun logisticService(logisticClient: LogisticClient, logisticDao: LogisticDao): LogisticService {
        return LogisticService(logisticClient, logisticDao)
    }
}
