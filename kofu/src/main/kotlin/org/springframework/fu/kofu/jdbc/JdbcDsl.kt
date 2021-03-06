package org.springframework.fu.kofu.jdbc

import org.springframework.boot.autoconfigure.jdbc.*
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.boot.jdbc.DataSourceInitializationMode
import org.springframework.context.support.GenericApplicationContext
import org.springframework.fu.kofu.AbstractDsl
import org.springframework.fu.kofu.ConfigurationDsl

class JdbcDsl(private val init: JdbcDsl.() -> Unit) : AbstractDsl() {

    var schema = listOf<String>()

    var data = listOf<String>()

    var initializationMode = DataSourceInitializationMode.EMBEDDED

    var url: String? = null

    var name: String? = null

    var username: String? = null

    var password: String? = null

    var generateUniqueName: Boolean = true


    override fun initialize(context: GenericApplicationContext) {
        super.initialize(context)
        init()

        val jdbcProperties = JdbcProperties()
        val dataSourceProperties = DataSourceProperties().apply {
            schema = this@JdbcDsl.schema
            data = this@JdbcDsl.data
            initializationMode = this@JdbcDsl.initializationMode
            url = this@JdbcDsl.url
            name = this@JdbcDsl.name
            username = this@JdbcDsl.username
            password = this@JdbcDsl.password
            isGenerateUniqueName = this@JdbcDsl.generateUniqueName
        }

        EmbeddedDataSourceConfigurationInitializer(dataSourceProperties).initialize(context)
        JdbcTemplateConfigurationInitializer(jdbcProperties).initialize(context)
        DataSourceTransactionManagerAutoConfigurationInitializer().initialize(context)
        DataSourceInitializerInvokerInitializer(dataSourceProperties).initialize(context)
    }
}

/**
 * Configure JDBC support.
 * @see JdbcDsl
 */
fun ConfigurationDsl.jdbc(dsl: JdbcDsl.() -> Unit = {}) {
    JdbcDsl(dsl).initialize(context)
}
