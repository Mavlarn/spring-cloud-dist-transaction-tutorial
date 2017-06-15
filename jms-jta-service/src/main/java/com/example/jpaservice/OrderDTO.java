package com.example.jpaservice;

import java.io.Serializable;

/**
 * Created by mavlarn on 2017/5/16.
 */
public class OrderDTO implements Serializable {

    private Long id;

    private String title;

    private String ticketIds;

    private String status;

    private int error;

    private String token;

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

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ticketIds='" + ticketIds + '\'' +
                ", status='" + status + '\'' +
                ", error='" + error + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
