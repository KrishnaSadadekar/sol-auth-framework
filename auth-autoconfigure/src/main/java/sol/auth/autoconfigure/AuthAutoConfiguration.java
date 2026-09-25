package sol.auth.autoconfigure;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfiguration;
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
    @ConfigurationProperties(prefix = "auth.datasource")
    public DataSourceProperties authDataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        // Fallback default so LMS doesn't even need auth.datasource in its yml if
        // defaults match
        properties.setUrl(
                "jdbc:mysql://localhost:3306/auth_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata");
        properties.setUsername("root");
        properties.setPassword("root");
        properties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return properties;
    }

    @Bean(name = "authDataSource")
    public DataSource authDataSource() {
        return authDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "authEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(authDataSource())
                .packages("sol.auth.entity") // Package where your auth entities live
                .persistenceUnit("authPersistenceUnit")
                .build();
    }

    @Bean(name = "authTransactionManager")
    public PlatformTransactionManager authTransactionManager(
            EntityManagerFactory authEntityManagerFactory) {
        return new JpaTransactionManager(authEntityManagerFactory);
    }

}