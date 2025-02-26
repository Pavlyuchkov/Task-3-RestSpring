package springApp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;


@Configuration
@ComponentScan("springApp")
@PropertySource({"classpath:config.yaml"})
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "springApp.repository")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    @Value("${database.driver}")
    private String driver;

    @Value("${database.url}")
    private String url;

    @Value("${database.user}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Value("${hibernate.show_sql}")
    private String show_sql;

    @Value("${hibernate.format_sql}")
    private String formatSql;

    @Value("${liquibase.changelog}")
    private String liquibaseChangeLog;

    @Bean(name = "beanFactoryPostProcessor")
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        PropertySourcesPlaceholderConfigurer configure = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("config.yaml"));
        Properties yamlObject = Objects.requireNonNull(yaml.getObject(), "Не найден файл config.yaml");
        configure.setProperties(yamlObject);

        System.out.println("beanFactoryBean created");

        return configure;
    }

    @Bean(name = "properties")
    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", show_sql);
        properties.put("hibernate.format_sql", formatSql);

        System.out.println("propertiesBean created");

        return properties;
    }

    @Bean(name = "liquibase")
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog(liquibaseChangeLog);

        System.out.println("Liquibase bean created");

        return liquibase;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        System.out.println("dataSourceBean created");

        return new HikariDataSource(config);
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaProperties(jpaProperties());
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("springApp/model");

        System.out.println("entityManagerFactoryBean created");

        return entityManagerFactory;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        System.out.println("transactionManagerBean created");

        return transactionManager;
    }

}
