package com.comparison.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PostgresData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "COMPANY_CODE")
    private String COMPANY_CODE;

    @Column(name = "RECEIVE_NUMBER")
    private Long RECEIVE_NUMBER;

    @Column(name = "RECEIVE_DATE")
    private String RECEIVE_DATE;

    @Column(name = "ORDER_LAYOUT_TYPE")
    private String ORDER_LAYOUT_TYPE;

    @Column(name = "TOTAL_GROSS_AMOUNT")
    private Double TOTAL_GROSS_AMOUNT;

    @Column(name = "TOTAL_DISCOUNT_AMOUNT")
    private Double TOTAL_DISCOUNT_AMOUNT;

    @Column(name = "TOTAL_NET_AMOUNT")
    private Double TOTAL_NET_AMOUNT;

    @Column(name = "TOTAL_TAX_AMOUNT")
    private Double TOTAL_TAX_AMOUNT;

    @Column(name = "PACKING_POSTING_AMOUNT")
    private Double PACKING_POSTING_AMOUNT;

    @Column(name = "PACKING_POSTING_TAX_AMOUNT")
    private Double PACKING_POSTING_TAX_AMOUNT;

    @Column(name = "TOTAL_INVOICE_AMOUNT")
    private Double TOTAL_INVOICE_AMOUNT;

    @Column(name = "TOTAL_DETAIL_LINE_COUNT")
    private Double TOTAL_DETAIL_LINE_COUNT;

}
