package entity.consume;

import com.alibaba.fastjson.JSONObject;

import javax.xml.stream.events.DTD;
import java.util.Date;

public class AddConsume {
    private String memberId;
    private String paymentOption;   //支付方式
    private String paymentAmount;   //支付数量
    private String cart;
    private Date time;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCart() {
        return cart;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public AddConsume(String memberId, String paymentOption, String paymentAmount, String cart){
        this.memberId = memberId;
        this.paymentOption = paymentOption;
        this.paymentAmount = paymentAmount;
        this.cart = cart;
        java.util.Date currentDate = new java.util.Date();
        this.time = new Date(currentDate.getTime());
    }
}
