package com.comparison.database.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "data")
@NoArgsConstructor
@AllArgsConstructor
public class MongoData {

  @Id private ObjectId id;

  @Field("COMPANY_CODE")
  private String companyCode;

  @Field("RECEIVE_NUMBER")
  private Long receiveNumber;

  @Field("RECEIVE_DATE")
  private String receiveDate;

  @Field("ORDER_LAYOUT_TYPE")
  private String orderLayoutType;

  @Field("TOTAL_GROSS_AMOUNT")
  private Double totalGrossAmount;

  @Field("TOTAL_DISCOUNT_AMOUNT")
  private Double totalDiscountAmount;

  @Field("TOTAL_NET_AMOUNT")
  private Double totalNetAmount;

  @Field("TOTAL_TAX_AMOUNT")
  private Double totalTaxAmount;

  @Field("PACKING_POSTING_AMOUNT")
  private Double packingPostingAmount;

  @Field("PACKING_POSTING_TAX_AMOUNT")
  private Double packingPostingTaxAmount;

  @Field("TOTAL_INVOICE_AMOUNT")
  private Double totalInvoiceAmount;

  @Field("TOTAL_DETAIL_LINE_COUNT")
  private Double totalDetailLineCount;
}
