package sol.auth.autoconfigure;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import sol.auth.jwt.configuration.JwtTokenProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;

@AutoConfiguration
@EnableConfigurationProperties({
        AuthPlatformProperties.class,
        JwtTokenProperties.class
})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = "sol.auth.core.repository")
@EntityScan(basePackages = "sol.auth.core.entity")
@EnableAsync
@ComponentScan(basePackages = {
        "sol.auth.core.configuration",
        "sol.auth.core.service.implementation",
        "sol.auth.security.filter",
        "sol.auth.security.provider",
        "sol.auth.security.service",
        "sol.auth.jwt.service.implementation",
        "sol.auth.service"
})
public class AuthAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "auth.datasource", name = "url")
    @ConfigurationProperties(prefix = "auth.datasource")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "authDataSource")
    @ConditionalOnProperty(prefix = "auth.datasource", name = "url")
    public DataSource authDataSource(@Qualifier("authDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "authEntityManagerFactory")
    @ConditionalOnProperty(prefix = "auth.datasource", name = "url")
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("authDataSource") DataSource authDataSource) {
        return builder
                .dataSource(authDataSource)
                .packages("sol.auth.core.entity")
                .persistenceUnit("authPersistenceUnit")
                .build();
    }

    @Bean(name = "authTransactionManager")
    @ConditionalOnProperty(prefix = "auth.datasource", name = "url")
    public PlatformTransactionManager authTransactionManager(
            @Qualifier("authEntityManagerFactory") EntityManagerFactory authEntityManagerFactory) {
        return new JpaTransactionManager(authEntityManagerFactory);
    }
}