package com.mc851.xcommerce.conf

import com.mc851.xcommerce.clients.*
import com.mc851.xcommerce.clients.address.AddressClientOkHttp
import com.mc851.xcommerce.clients.credit.CreditClientOkHttp
import com.mc851.xcommerce.clients.logistic.LogisticClientOkHttp
import com.mc851.xcommerce.clients.payment.PaymentClientOkHttp
import com.mc851.xcommerce.clients.product01.ProductClientOkHttp
import com.mc851.xcommerce.clients.user01.UserClientOkHttp
import com.mc851.xcommerce.dao.category.CategoryDao
import com.mc851.xcommerce.dao.category.CategoryDaoPostgres
import com.mc851.xcommerce.dao.credential.UserCredentialDao
import com.mc851.xcommerce.dao.credential.UserCredentialDaoPostgres
import com.mc851.xcommerce.dao.logistic.LogisticDao
import com.mc851.xcommerce.dao.logistic.LogisticDaoPostgres
import com.mc851.xcommerce.dao.order.OrderDao
import com.mc851.xcommerce.dao.order.OrderDaoPostgres
import com.mc851.xcommerce.dao.order.OrderItemDao
import com.mc851.xcommerce.dao.order.OrderItemDaoPostgres
import com.mc851.xcommerce.dao.product.ProductDao
import com.mc851.xcommerce.dao.product.ProductDaoPostgres
import com.mc851.xcommerce.dao.user.UserDao
import com.mc851.xcommerce.dao.user.UserDaoPostgres
import com.mc851.xcommerce.dao.user.token.TokenDao
import com.mc851.xcommerce.dao.user.token.TokenDaoPostgres
import com.mc851.xcommerce.filters.ContextFilter
import com.mc851.xcommerce.filters.TokenManager
import com.mc851.xcommerce.service.cart.CartService
import com.mc851.xcommerce.service.cart.validators.CheckoutValidator
import com.mc851.xcommerce.service.logistic.LogisticService
import com.mc851.xcommerce.service.order.OrderService
import com.mc851.xcommerce.service.payment.PaymentService
import com.mc851.xcommerce.service.product.CategoryService
import com.mc851.xcommerce.service.product.ProductService
import com.mc851.xcommerce.service.user.AddressService
import com.mc851.xcommerce.service.user.CreditService
import com.mc851.xcommerce.service.user.UserService
import com.mc851.xcommerce.service.user.credential.UserCredentialService
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import javax.sql.DataSource

@Configuration
@PropertySource("classpath:application.properties")
class BeanConfiguration {

    @Autowired
    lateinit var environment: Environment

    // Adapter
    @Bean
    fun tokenManager(userCredentialService: UserCredentialService): TokenManager {
        return TokenManager(userCredentialService)
    }

    @Bean
    fun requestFilter(): ContextFilter {
        return ContextFilter()
    }

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
    fun userCredentialDao(jdbcTemplate: JdbcTemplate): UserCredentialDao {
        return UserCredentialDaoPostgres(jdbcTemplate)
    }

    @Bean
    fun tokenDao(jdbcTemplate: JdbcTemplate): TokenDao {
        return TokenDaoPostgres(jdbcTemplate)
    }

    @Bean
    fun logisticDao(jdbcTemplate: JdbcTemplate): LogisticDao {
        return LogisticDaoPostgres(jdbcTemplate)
    }

    @Bean
    fun orderDao(jdbcTemplate: JdbcTemplate): OrderDao {
        return OrderDaoPostgres(jdbcTemplate)
    }

    @Bean
    fun orderItemDao(jdbcTemplate: JdbcTemplate): OrderItemDao {
        return OrderItemDaoPostgres(jdbcTemplate)
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
    fun addressClient(): AddressClient {
        return AddressClientOkHttp()
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
    fun userCredentialService(userCredentialDao: UserCredentialDao, tokenDao: TokenDao): UserCredentialService {
        return UserCredentialService(userCredentialDao, tokenDao)
    }

    @Bean
    fun userService(userClient: UserClient,
                    userDao: UserDao,
                    userCredentialService: UserCredentialService): UserService {
        return UserService(userClient, userDao, userCredentialService)
    }

    @Bean
    fun paymentService(paymentClient: PaymentClient): PaymentService {
        return PaymentService(paymentClient)
    }

    @Bean
    fun creditService(creditClient: CreditClient): CreditService {
        return CreditService(creditClient)
    }

    @Bean
    fun addressService(addressClient: AddressClient): AddressService {
        return AddressService(addressClient)
    }

    @Bean
    fun logisticService(logisticClient: LogisticClient, logisticDao: LogisticDao): LogisticService {
        return LogisticService(logisticClient, logisticDao)
    }

    @Bean
    fun checkoutValidator(userService: UserService, addressService: AddressService, productService: ProductService): CheckoutValidator {
        return CheckoutValidator(userService, productService, addressService)
    }

    @Bean
    fun orderService(logisticService: LogisticService, orderDao: OrderDao, orderItemDao: OrderItemDao, productService: ProductService): OrderService {
        return OrderService(logisticService, orderDao, orderItemDao, productService)
    }

    @Bean
    fun cartService(checkoutValidator: CheckoutValidator,
                    orderService: OrderService,
                    paymentService: PaymentService,
                    userService: UserService): CartService {
        return CartService(checkoutValidator, orderService, paymentService, userService)
    }
}
