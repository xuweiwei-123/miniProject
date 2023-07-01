package entity.types;

import com.alibaba.fastjson.JSONObject;

public class ShowTypes {
    private String name;
    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ShowTypes(String name,int quantity){
        this.name = name;
        this.quantity = quantity;
    }
}
