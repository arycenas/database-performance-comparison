package com.comparison.database.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "data")
@NoArgsConstructor
@AllArgsConstructor
public class MongoData {

    @Id
    private ObjectId id;
    private String COMPANY_CODE;
    private Long RECEIVE_NUMBER;
    private String RECEIVE_DATE;
    private String ORDER_LAYOUT_TYPE;
    private Double TOTAL_GROSS_AMOUNT;
    private Double TOTAL_DISCOUNT_AMOUNT;
    private Double TOTAL_NET_AMOUNT;
    private Double TOTAL_TAX_AMOUNT;
    private Double PACKING_POSTING_AMOUNT;
    private Double PACKING_POSTING_TAX_AMOUNT;
    private Double TOTAL_INVOICE_AMOUNT;
    private Double TOTAL_DETAIL_LINE_COUNT;
}
