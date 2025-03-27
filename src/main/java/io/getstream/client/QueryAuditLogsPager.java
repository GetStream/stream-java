package io.getstream.client;

public class QueryAuditLogsPager {
    private String next;
    private String prev;
    private int limit;
    
    public QueryAuditLogsPager() {
    }
    
    public QueryAuditLogsPager(int limit) {
        this.limit = limit;
    }
    
    public QueryAuditLogsPager(String next, String prev, int limit) {
        this.next = next;
        this.prev = prev;
        this.limit = limit;
    }
    
    public String getNext() {
        return next;
    }
    
    public void setNext(String next) {
        this.next = next;
    }
    
    public String getPrev() {
        return prev;
    }
    
    public void setPrev(String prev) {
        this.prev = prev;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public void setLimit(int limit) {
        this.limit = limit;
    }
} 