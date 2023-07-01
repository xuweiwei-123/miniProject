package entity.types;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

public class ShowConsume {
    private String type;
    private int quantity;
    private BigDecimal amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public ShowConsume(String type,int quantity,BigDecimal amount){
        this.type = type;
        this.quantity = quantity;
        this.amount = amount;
    }
}
