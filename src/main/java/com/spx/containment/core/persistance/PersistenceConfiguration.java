package com.spx.containment.core.persistance;


import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.spx.containment")
@EnableTransactionManagement
public class PersistenceConfiguration {

    @Value("${application.persistence.user}")
    String persistenceUser;
    @Value("${application.persistence.password}")
    String persistencePassword;
    @Value("${application.persistence.host}")
    String persistenceHost;
    @Value("${application.persistence.pool-size}")
    int persistencePoolSize;
    private SessionFactory sessionFactory;

    @Bean
    public synchronized SessionFactory sessionFactory() {
        // with domain entity base package(s)
        if (sessionFactory == null) {
            sessionFactory = new SessionFactory(configuration(), "com.spx.containment.core");

        }
        return sessionFactory;
    }

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        String uri = String.format("bolt://%s:%s@%s", persistenceUser, persistencePassword, persistenceHost);
        return new org.neo4j.ogm.config.Configuration.Builder().uri(uri).connectionPoolSize(persistencePoolSize)

            .build();
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }

}
