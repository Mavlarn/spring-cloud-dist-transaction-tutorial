package com.example.jpaservice;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.TransactionAwareConnectionFactoryProxy;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
@EnableTransactionManagement
public class JmsJtaServiceTxAwareApplication {

    private static final Logger LOG = LoggerFactory.getLogger(JmsJtaServiceTxAwareApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(JmsJtaServiceTxAwareApplication.class, args);
    }

    @Bean
    @Autowired
    public JmsTemplate initJmsTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonJmsMessageConverter) {
        LOG.debug("init jms template with converter.");
        JmsTemplate template = new JmsTemplate();
        template.setMessageConverter(jacksonJmsMessageConverter);
        template.setConnectionFactory(connectionFactory);
//        template.setSessionTransacted(false); // will always use transacted session in spring Transaction
        return template;
    }

    @Bean
    public ConnectionFactory transactionAwareCF() {
        LOG.debug("init Transaction aware ConnectionFactory.");
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        TransactionAwareConnectionFactoryProxy cfProxy = new TransactionAwareConnectionFactoryProxy(cf);
        cfProxy.setSynchedLocalTransactionAllowed(true);
        return cfProxy;
    }

    @Bean
    public JmsListenerContainerFactory<?> orderFactory(ConnectionFactory connectionFactory,
                                                       DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}
