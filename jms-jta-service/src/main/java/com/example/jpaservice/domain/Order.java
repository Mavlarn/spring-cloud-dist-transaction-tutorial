package com.example.jpaservice.domain;

import javax.persistence.*;

/**
 * Created by mavlarn on 2017/5/16.
 */
@Entity
@Table(name = "CustomerOrder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String ticketIds;

    @Column
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(String ticketIds) {
        this.ticketIds = ticketIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ticketIds='" + ticketIds + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
