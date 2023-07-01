package entity.users;

import com.alibaba.fastjson.JSONObject;

public class ReqWrapperBodyChange {

    private String currentUsername;
    private String currentPassword;
    private String newPassword;
    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public ReqWrapperBodyChange(String currentUsername, String currentPassword, String newPassword) {
        this.currentUsername = currentUsername;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
