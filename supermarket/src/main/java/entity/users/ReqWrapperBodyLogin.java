package entity.users;


import com.alibaba.fastjson.JSONObject;

public class ReqWrapperBodyLogin {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public ReqWrapperBodyLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
