package com.banking_application.dto;

import java.math.BigDecimal;

public class AmountDTO {
    private BigDecimal amount;

    public BigDecimal getAmount(){
        return amount;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
}
