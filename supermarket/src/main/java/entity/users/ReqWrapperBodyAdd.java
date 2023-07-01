package entity.users;

import com.alibaba.fastjson.JSONObject;

public class ReqWrapperBodyAdd {
    private String addUserID;
    private String addUserName;
    private String addUserPassword;
    private String addUserSex;
    private String addUserAge;

    public String getAddUserID() {
        return addUserID;
    }

    public void setAddUserID(String addUserID) {
        this.addUserID = addUserID;
    }

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName;
    }

    public String getAddUserPassword() {
        return addUserPassword;
    }

    public void setAddUserPassword(String addUserPassword) {
        this.addUserPassword = addUserPassword;
    }

    public String getAddUserSex() {
        return addUserSex;
    }

    public void setAddUserSex(String addUserSex) {
        this.addUserSex = addUserSex;
    }

    public String getAddUserAge() {
        return addUserAge;
    }

    public void setAddUserAge(String addUserAge) {
        this.addUserAge = addUserAge;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public ReqWrapperBodyAdd(String addUserID,String addUserName,String addUserPassword,String addUserSex,String addUserAge){
        this.addUserID = addUserID;
        this.addUserName = addUserName;
        this.addUserPassword = addUserPassword;
        this.addUserSex = addUserSex;
        this.addUserAge = addUserAge;
    }
    public ReqWrapperBodyAdd(String addUserID,String addUserName,String addUserSex,String addUserAge){
        this.addUserID = addUserID;
        this.addUserName = addUserName;
        this.addUserSex = addUserSex;
        this.addUserAge = addUserAge;
    }
}
