package entity.members;

import com.alibaba.fastjson.JSONObject;

public class MemberAdd {
    private String addMemberID;
    private String addMemberName;
    private String addMemberPassword;
    private String addMemberSex;
    private String addMemberAge;
    private String addMemberConsumer;

    public String getAddMemberID() {
        return addMemberID;
    }

    public void setAddMemberID(String addMemberID) {
        this.addMemberID = addMemberID;
    }

    public String getAddMemberName() {
        return addMemberName;
    }

    public void setAddMemberName(String addMemberName) {
        this.addMemberName = addMemberName;
    }

    public String getAddMemberPassword() {
        return addMemberPassword;
    }

    public void setAddMemberPassword(String addMemberPassword) {
        this.addMemberPassword = addMemberPassword;
    }

    public String getAddMemberSex() {
        return addMemberSex;
    }

    public void setAddMemberSex(String addMemberSex) {
        this.addMemberSex = addMemberSex;
    }

    public String getAddMemberAge() {
        return addMemberAge;
    }

    public void setAddMemberAge(String addMemberAge) {
        this.addMemberAge = addMemberAge;
    }

    public String getAddMemberConsumer() {
        return addMemberConsumer;
    }

    public void setAddMemberConsumer(String addMemberConsumer) {
        this.addMemberConsumer = addMemberConsumer;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public MemberAdd(String addMemberID,String addMemberName,String addMemberPassword,String addMemberSex,String addMemberAge){
        this.addMemberID = addMemberID;
        this.addMemberName = addMemberName;
        this.addMemberPassword = addMemberPassword;
        this.addMemberSex = addMemberSex;
        this.addMemberAge = addMemberAge;
    }
    public MemberAdd(String addMemberID,String addMemberName,String addMemberSex,String addMemberAge){
        this.addMemberID = addMemberID;
        this.addMemberName = addMemberName;
        this.addMemberSex = addMemberSex;
        this.addMemberAge = addMemberAge;
    }
}
