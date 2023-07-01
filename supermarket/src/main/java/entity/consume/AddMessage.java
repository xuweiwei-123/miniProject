package entity.consume;

import com.alibaba.fastjson.JSONObject;

public class AddMessage {
    private String productId;
    private int quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public AddMessage(String productId,int quantity){
        this.productId = productId;
        this.quantity = quantity;
    }
}
