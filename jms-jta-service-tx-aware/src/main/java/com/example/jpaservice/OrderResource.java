package com.example.jpaservice;

import com.example.jpaservice.domain.Order;
import com.example.jpaservice.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        jmsTemplate.setReceiveTimeout(5000);
    }

    @PostMapping(value = "/")
//    @Transactional
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
    public OrderDTO getAllFromMessageNew() {
        return (OrderDTO)jmsTemplate.receiveAndConvert("order:new");
    }

    @GetMapping(value = "/jms/pay")
    public OrderDTO getAllFromMessagePay() {
        return (OrderDTO)jmsTemplate.receiveAndConvert("order:need_to_pay");
    }

}
