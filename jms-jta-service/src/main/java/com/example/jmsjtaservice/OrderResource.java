package com.example.jmsjtaservice;

import com.example.jmsjtaservice.domain.Order;
import com.example.jmsjtaservice.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by mavlarn on 2017/5/16.
 */
@RestController
@RequestMapping("/api/order")
public class OrderResource {
    private static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping(value = "/")
    @Transactional
    public void create(@RequestBody OrderDTO orderDTO) {
        LOG.debug("Create order:{}", orderDTO);
        String uid = UUID.randomUUID().toString();
        orderDTO.setToken(uid);
        jmsTemplate.convertAndSend("order:new", orderDTO);
    }

    @GetMapping(value = "/")
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Order get(@PathVariable Long id) {
        return orderRepository.findOne(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        orderRepository.delete(id);
    }

    @GetMapping(value = "/jms/new")
    public Order getAllFromMessageNew() {
        return (Order)jmsTemplate.receiveAndConvert("order:new");
    }

    @GetMapping(value = "/jms/pay")
    public Order getAllFromMessagePay() {
        return (Order)jmsTemplate.receiveAndConvert("order:need_to_pay");
    }

}
