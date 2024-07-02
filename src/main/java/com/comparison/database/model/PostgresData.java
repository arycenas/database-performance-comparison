package com.comparison.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PostgresData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "COMPANY_CODE")
    private String COMPANY_CODE;

    @Column(name = "RECEIVE_NUMBER")
    private String RECEIVE_NUMBER;

    @Column(name = "RECEIVE_DATE")
    private String RECEIVE_DATE;

    @Column(name = "ORDER_LAYOUT_TYPE")
    private String ORDER_LAYOUT_TYPE;

    @Column(name = "TOTAL_GROSS_AMOUNT")
    private String TOTAL_GROSS_AMOUNT;

    @Column(name = "TOTAL_DISCOUNT_AMOUNT")
    private String TOTAL_DISCOUNT_AMOUNT;

    @Column(name = "TOTAL_NET_AMOUNT")
    private String TOTAL_NET_AMOUNT;

    @Column(name = "TOTAL_TAX_AMOUNT")
    private String TOTAL_TAX_AMOUNT;

    @Column(name = "PACKING_POSTING_AMOUNT")
    private String PACKING_POSTING_AMOUNT;

    @Column(name = "PACKING_POSTING_TAX_AMOUNT")
    private String PACKING_POSTING_TAX_AMOUNT;

    @Column(name = "TOTAL_INVOICE_AMOUNT")
    private String TOTAL_INVOICE_AMOUNT;

    @Column(name = "TOTAL_DETAIL_LINE_COUNT")
    private String TOTAL_DETAIL_LINE_COUNT;

}
