
package com.huahui.datasphere.mdm.system.configuration;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.camel.management.JmxManagementStrategyFactory;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.huahui.datasphere.mdm.system.service.PlatformConfiguration;

import bitronix.tm.TransactionManagerServices;

/**
 * @author theseusyang
 * Root spring context link.
 */
@Configuration
@PropertySource("classpath:conf/backend.properties")
public class SystemConfiguration extends AbstractConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemConfiguration.class);

    private static final ConfigurationId ID = () -> "SYSTEM_CONFIGURATION";

    private ResourceBundleMessageSource systemMessageSource;

    /**
     * Constructor.
     */
    public SystemConfiguration() {
        super();

        // Do it here, because otherwise, we can not control bundles joining
        systemMessageSource = new ResourceBundleMessageSource();
        systemMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    }

    /**
     * Link to system bundles.
     */
    public ResourceBundleMessageSource getSystemMessageSource() {
        return systemMessageSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConfigurationId getId() {
        return ID;
    }

    public static ApplicationContext getApplicationContext() {
        return CONFIGURED_CONTEXT_MAP.get(ID);
    }
    /**
     * Gets a bean.
     *
     * @param <T>
     * @param beanClass the bean class
     * @return bean
     */
    public static <T> T getBean(Class<T> beanClass) {
        if (CONFIGURED_CONTEXT_MAP.containsKey(ID)) {
            return CONFIGURED_CONTEXT_MAP.get(ID).getBean(beanClass);
        }

        return null;
    }

    /**
     * Gets beans of type.
     *
     * @param <T>
     * @param beanClass the bean class
     * @return bean
     */
    public static <T> Map<String, T> getBeans(Class<T> beanClass) {
        if (CONFIGURED_CONTEXT_MAP.containsKey(ID)) {
            return CONFIGURED_CONTEXT_MAP.get(ID).getBeansOfType(beanClass);
        }

        return Collections.emptyMap();
    }

    @Bean
    public HazelcastInstance hazelcastInstance(
            @Value("${" + SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_PORT + ":5701}") final int port,
            @Value("${" + SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_TCP_IP_ENABLED + ":false}") final boolean tpcIpEnabled,
            @Value("${" + SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_MULTICAST_ENABLED + ":false}") final boolean multicastEnabled) {

        final Config unidataHzConfig = new Config()
                .setInstanceName("unidata");

        final JoinConfig join = unidataHzConfig.getNetworkConfig()
                .setPort(port)
                .setPortAutoIncrement(false)
                .getJoin();

        join.getTcpIpConfig().setEnabled(tpcIpEnabled);
        join.getMulticastConfig().setEnabled(multicastEnabled);
        join.getAwsConfig().setEnabled(false);
        HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName("unidata");
        if(instance!=null)
        	return instance;
        else
        return Hazelcast.getOrCreateHazelcastInstance(unidataHzConfig);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        final PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
                new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setEnvironment(new StandardEnvironment());
        return propertySourcesPlaceholderConfigurer;
    }

    @Bean("systemDataSource")
    public DataSource systemDataSource(@Autowired Environment env) {

        // Single connection data source
        String url = env.getProperty(SystemConfigurationConstants.PROPERTY_SYSTEM_DATASOURCE_URL);
        String username = env.getProperty(SystemConfigurationConstants.PROPERTY_SYSTEM_DATASOURCE_USER);
        String password = env.getProperty(SystemConfigurationConstants.PROPERTY_SYSTEM_DATASOURCE_PASSWORD);

        Objects.requireNonNull(url, "System datasource URL cannot be null");

        SingleConnectionDataSource scds = new SingleConnectionDataSource(url, username, password, true);
        scds.setDriverClassName("org.postgresql.Driver");

        return scds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(PlatformConfiguration pc) {

        bitronix.tm.Configuration btmc = TransactionManagerServices.getConfiguration();
        Properties properties = getAllPropertiesWithPrefix(SystemConfigurationConstants.PROPERTY_BITRONIX_PREFIX, true);
        if (Objects.nonNull(properties)) {

            properties.forEach((k, v) -> {

                // Property names differ
                String propName = StringUtils.substringAfterLast(k.toString(), ".");
                if ("maxLogSize".equals(propName)) {
                    propName = "maxLogSizeInMb";
                } else if ("async".equals(propName)) {
                    propName = "asynchronous2Pc";
                } else if ("sync".equals(propName)) {
                    propName = "synchronousJmxRegistration";
                } else if ("userTransactionName".equals(propName)) {
                    propName = "jndiUserTransactionName";
                } else if ("transactionSynchronizationRegistryName".equals(propName)) {
                    propName = "jndiTransactionSynchronizationRegistryName";
                } else if ("configuration".equals(propName)) {
                    propName = "resourceConfigurationFilename";
                }

                try {
                    // Overwrite non-null values only
                    Class<?> targetClazz = PropertyUtils.getPropertyType(btmc, propName);
                    if (targetClazz != null) {

                        String stringVal = Objects.nonNull(v) ? v.toString() : null;
                        if (StringUtils.isBlank(stringVal)) {
                            return;
                        }

                        if (targetClazz == String.class) {
                            PropertyUtils.setProperty(btmc, propName, stringVal);
                        } else {

                            Object targetValue = ConvertUtils.convert(stringVal, targetClazz);
                            if (targetValue != stringVal) {
                                PropertyUtils.setProperty(btmc, propName, targetValue);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.warn("Cannot wire property value to BITRONIX configuration.", e);
                }
            });
        }

        btmc.setServerId("BTM-" + pc.getNodeId());
        if ("auto".equals(btmc.getJdbcProxyFactoryClass())) {
            btmc.setJdbcProxyFactoryClass("bitronix.tm.resource.jdbc.proxy.JdbcJavaProxyFactory");
        }

        JtaTransactionManager jtm = new bitronix.tm.integration.spring.PlatformTransactionManager();
        jtm.setAllowCustomIsolationLevels(true);

        return jtm;
    }

    @Bean("configuration-sql")
    public PropertiesFactoryBean configurationSql() {
        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/db/configuration-sql.xml"));
        return propertiesFactoryBean;
    }

    @Bean("pipelines-sql")
    public PropertiesFactoryBean pipelinesSql() {
        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/db/pipelines-sql.xml"));
        return propertiesFactoryBean;
    }

    @Bean
    public MessageSource messageSource() {
        return systemMessageSource;
    }

    @Bean
    public JmxManagementStrategyFactory jmxManagementStrategyFactory() {
        return new JmxManagementStrategyFactory();
    }

    @Bean
    public GroupedBodyAggregationStrategy groupedBodyAggregationStrategy() {
        return new GroupedBodyAggregationStrategy();
    }
}
