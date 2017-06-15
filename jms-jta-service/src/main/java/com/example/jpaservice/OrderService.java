package com.example.jpaservice;

import com.example.jpaservice.domain.Order;
import com.example.jpaservice.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mavlarn on 2017/5/27.
 */
@Service
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    private Set<String> processedUIDs = new HashSet<>();

    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    OrderRepository orderRepository;

    @JmsListener(destination = "order:new", containerFactory = "orderFactory")
    @Transactional
    public void create(OrderDTO orderDTO) {
        LOG.debug("Get jms message to create order:{}", orderDTO);
        if (!this.processedUIDs.contains(orderDTO.getToken())) {
            Order order = new Order();
            order.setTicketIds(orderDTO.getTicketIds());
            order.setTitle(orderDTO.getTitle());
            order.setStatus("PENDING");
            if (orderDTO.getError() == 1) {
                throw new RuntimeException("Error1");
            }
            orderRepository.save(order);
            if (orderDTO.getError() == 2) {
                throw new RuntimeException("Error2");
            }
            orderDTO.setStatus(order.getStatus());
            orderDTO.setId(order.getId());

        } else {
            LOG.info("Duplicate jms message:{}", orderDTO);
        }

        this.jmsTemplate.convertAndSend("order:need_to_pay", orderDTO);

        if (orderDTO.getError() == 3) {
            throw new RuntimeException("Error3");
        }
        processedUIDs.add(orderDTO.getToken());
    }

}
