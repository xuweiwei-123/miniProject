package entity.goods;

import com.alibaba.fastjson.JSONObject;

public class GoodsAdd {
    private String addGoodsID;
    private String addGoodsName;
    private String addGoodsType;
    private double addGoodsPrice;
    private int addGoodsNumber;

    public String getAddGoodsID() {
        return addGoodsID;
    }

    public void setAddGoodsID(String addGoodsID) {
        this.addGoodsID = addGoodsID;
    }

    public String getAddGoodsName() {
        return addGoodsName;
    }

    public void setAddGoodsName(String addGoodsName) {
        this.addGoodsName = addGoodsName;
    }

    public String getAddGoodsType() {
        return addGoodsType;
    }

    public void setAddGoodsType(String addGoodsType) {
        this.addGoodsType = addGoodsType;
    }

    public double getAddGoodsPrice() {
        return addGoodsPrice;
    }

    public void setAddGoodsPrice(double addGoodsPrice) {
        this.addGoodsPrice = addGoodsPrice;
    }

    public int getAddGoodsNumber() {
        return addGoodsNumber;
    }

    public void setAddGoodsNumber(int addGoodsNumber) {
        this.addGoodsNumber = addGoodsNumber;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public GoodsAdd(String addGoodsID,String addGoodsName,String addGoodsType,double addGoodsPrice,int addGoodsNumber){
        this.addGoodsID = addGoodsID;
        this.addGoodsName = addGoodsName;
        this.addGoodsType = addGoodsType;
        this.addGoodsPrice = addGoodsPrice;
        this.addGoodsNumber = addGoodsNumber;
    }
}
