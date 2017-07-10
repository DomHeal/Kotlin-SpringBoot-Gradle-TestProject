package com.dominicheal.configuration

/**
 * @author Dominic
 * @since  09-Jul-17
 * Website: www.dominicheal.com
 * Github: www.github.com/DomHeal
 */

import com.zaxxer.hikari.HikariDataSource
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.naming.NamingException
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


@Configuration
@EnableJpaRepositories(basePackages = arrayOf("com.dominicheal.repositories"), entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@EnableTransactionManagement
class JpaConfiguration {

    @Autowired
    private val environment: Environment? = null

    @Value("\${datasource.sampleapp.maxPoolSize:10}")
    private val maxPoolSize: Int = 0

    /*
     * Populate SpringBoot DataSourceProperties object directly from application.yml
     * based on prefix.Thanks to .yml, Hierachical data is mapped out of the box with matching-name
     * properties of DataSourceProperties object].
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.sampleapp")
    fun dataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    /*
     * Configure HikariCP pooled DataSource.
     */
    @Bean
    fun dataSource(): DataSource {
        val dataSourceProperties = dataSourceProperties()
        val dataSource = DataSourceBuilder
                .create(dataSourceProperties.classLoader)
                .driverClassName(dataSourceProperties.driverClassName)
                .url(dataSourceProperties.url)
                .username(dataSourceProperties.username)
                .password(dataSourceProperties.password)
                .type(HikariDataSource::class.java)
                .build() as HikariDataSource
        dataSource.maximumPoolSize = maxPoolSize
        return dataSource
    }

    /*
     * Entity Manager Factory setup.
     */
    @Bean
    @Throws(NamingException::class)
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val factoryBean = LocalContainerEntityManagerFactoryBean()
        factoryBean.dataSource = dataSource()
        factoryBean.setPackagesToScan(*arrayOf("com.websystique.springboot.model"))
        factoryBean.jpaVendorAdapter = jpaVendorAdapter()
        factoryBean.setJpaProperties(jpaProperties())
        return factoryBean
    }

    /*
     * Provider specific adapter.
     */
    @Bean
    fun jpaVendorAdapter(): JpaVendorAdapter {
        val hibernateJpaVendorAdapter = HibernateJpaVendorAdapter()
        return hibernateJpaVendorAdapter
    }

    /*
     * Here you can specify any provider specific properties.
     */
    private fun jpaProperties(): Properties {
        val properties = Properties()
        properties.put("hibernate.dialect", environment!!.getRequiredProperty("datasource.sampleapp.hibernate.dialect"))
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("datasource.sampleapp.hibernate.hbm2ddl.method"))
        properties.put("hibernate.show_sql", environment.getRequiredProperty("datasource.sampleapp.hibernate.show_sql"))
        properties.put("hibernate.format_sql", environment.getRequiredProperty("datasource.sampleapp.hibernate.format_sql"))
        if (StringUtils.isNotEmpty(environment.getRequiredProperty("datasource.sampleapp.defaultSchema"))) {
            properties.put("hibernate.default_schema", environment.getRequiredProperty("datasource.sampleapp.defaultSchema"))
        }
        return properties
    }

    @Bean
    @Autowired
    fun transactionManager(emf: EntityManagerFactory): PlatformTransactionManager {
        val txManager = JpaTransactionManager()
        txManager.entityManagerFactory = emf
        return txManager
    }

}