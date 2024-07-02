package com.comparison.database.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(collection = "data")
public class MongoData {

    @Id
    private ObjectId id;
    private String COMPANY_CODE;
    private String RECEIVE_NUMBER;
    private String RECEIVE_DATE;
    private String ORDER_LAYOUT_TYPE;
    private String TOTAL_GROSS_AMOUNT;
    private String TOTAL_DISCOUNT_AMOUNT;
    private String TOTAL_NET_AMOUNT;
    private String TOTAL_TAX_AMOUNT;
    private String PACKING_POSTING_AMOUNT;
    private String PACKING_POSTING_TAX_AMOUNT;
    private String TOTAL_INVOICE_AMOUNT;
    private String TOTAL_DETAIL_LINE_COUNT;
}
