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
  private String companyCode;

  @Column(name = "RECEIVE_NUMBER")
  private Long receiveNumber;

  @Column(name = "RECEIVE_DATE")
  private String receiveDate;

  @Column(name = "ORDER_LAYOUT_TYPE")
  private String orderLayoutType;

  @Column(name = "TOTAL_GROSS_AMOUNT")
  private Double totalGrossAmount;

  @Column(name = "TOTAL_DISCOUNT_AMOUNT")
  private Double totalDiscountAmount;

  @Column(name = "TOTAL_NET_AMOUNT")
  private Double totalNetAmount;

  @Column(name = "TOTAL_TAX_AMOUNT")
  private Double totalTaxAmount;

  @Column(name = "PACKING_POSTING_AMOUNT")
  private Double packingPostingAmount;

  @Column(name = "PACKING_POSTING_TAX_AMOUNT")
  private Double packingPostingTaxAmount;

  @Column(name = "TOTAL_INVOICE_AMOUNT")
  private Double totalInvoiceAmount;

  @Column(name = "TOTAL_DETAIL_LINE_COUNT")
  private Double totalDetailLineCount;
}
