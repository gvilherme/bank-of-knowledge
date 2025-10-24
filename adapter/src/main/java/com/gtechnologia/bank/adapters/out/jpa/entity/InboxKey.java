package com.gtechnologia.bank.adapters.out.jpa.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class InboxKey implements java.io.Serializable {
    private String consumerName; // ex: "account-client-registered"
    private String eventId;
    protected InboxKey(){}
    public InboxKey(String consumerName, String eventId){
        this.consumerName = consumerName; this.eventId = eventId;
    }
}
