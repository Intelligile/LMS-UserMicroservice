package com.example.UserMicroserviceAPI.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AuthorizationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // Unique code for authorization

    private double amount; // Credit amount
    private int periodMonths; // Time period in months
    private double totalCredit; // Total credit allowed
    private int productLimit; // Limit on number of products
    private boolean combined; // Is this a combination of amount and product?
    private Long licenseeId; // Assuming Licensee is another entity
    

    private LocalDate createdAt;
    @Column(name = "period_end_date")
    private LocalDate periodEndDate;
    // Foreign key reference to the Product table
    // @ManyToOne
    // @JoinColumn(name = "product_id")
    private Long product; // Assuming a Product entity exists
    private double discount; // Discount applicable
    public AuthorizationCode() {}

    public AuthorizationCode(String code, double amount, int periodMonths, double totalCredit, int productLimit, boolean combined, Long licenseeId ,LocalDate periodEndDate,Long product,double discount) {
        this.code = code;
        this.amount = amount;
        this.periodMonths = periodMonths;
        this.totalCredit = totalCredit;
        this.productLimit = productLimit;
        this.combined = combined;
        this.licenseeId = licenseeId;
        this.createdAt = LocalDate.now();
        this.periodEndDate = periodEndDate;
        this.product = product;
        this.discount = discount;

    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPeriodMonths() {
        return periodMonths;
    }

    public void setPeriodMonths(int periodMonths) {
        this.periodMonths = periodMonths;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public int getProductLimit() {
        return productLimit;
    }

    public void setProductLimit(int productLimit) {
        this.productLimit = productLimit;
    }

    public boolean isCombined() {
        return combined;
    }

    public void setCombined(boolean combined) {
        this.combined = combined;
    }

    public Long getLicenseeId() {
        return licenseeId;
    }

    public void setLicenseeId(Long licenseeId) {
        this.licenseeId = licenseeId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }
    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

     // Getters and Setters for the new fields
     public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
