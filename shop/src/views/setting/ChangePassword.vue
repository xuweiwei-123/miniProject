<template>
  <div class="set-content">
    <h2 style="font-size: 30px;">修  改  密  码</h2>
  <div class="set-form">
    <form @submit.prevent="changePassword">
      <label for="currentUsername" class="set-label">当前用户：</label>
      <input type="text" id="currentUsername" class="set-input" v-model="currentUsername" required>
      <br>
      <br>
      <label for="currentPassword" class="set-label">当前密码：</label>
      <input type="password" id="currentPassword" class="set-input" v-model="currentPassword" required>
      <br>
      <br>
      <label for="newPassword" class="set-label">新密码：</label>
      <input type="password" id="newPassword" class="set-input" v-model="newPassword" required>
      <br>
      <br>
      <label for="confirmPassword" class="set-label">确认密码：</label>
      <input type="password" id="confirmPassword" class="set-input" v-model="confirmPassword" required>
      <br>
      <br>
      <button type="submit" class="password-button">确认修改</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      currentUsername: '',
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
      error: ''
    };
  },
  methods: {
    changePassword() {
      // 进行密码修改的逻辑
      if (this.newPassword !== this.confirmPassword) {
        this.error = '新密码和确认密码不一致';
        return;
      }

      //发送密码修改请求到后端
      //替换以下代码为实际的密码修改请求
      axios.post('http://localhost:8080/supermarket/ChangePasswordServlet', {
        currentUsername: this.currentUsername,
        currentPassword: this.currentPassword,
        newPassword: this.newPassword
      })
        .then(response => {
          // 处理密码修改成功的逻辑
          console.log(response.data);
          // 清空表单字段
          this.currentUsername = '';
          this.currentPassword = '';
          this.newPassword = '';
          this.confirmPassword = '';
          alert("修改密码成功！");
        })
        .catch(error => {
          // 处理密码修改失败的逻辑
          console.error(error);
          this.error = '密码修改失败';
        });
    }
  }
};
</script>

<style>
.set-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 100px);
  background-color: rgb(242, 221, 242);
}

h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.set-form {
  width: 300px;
}

.set-label {
  display: block;
  font-weight: bold;
  margin-bottom: 10px;
}

.set-input {
  width: 100%;
  padding: 5px;
  margin-bottom: 10px;
}

.password-button {
  width: 100%;
  padding: 10px;
  background-color: rgb(202, 202, 253);
  color: white;
  border: none;
  cursor: pointer;
}

.error {
  color: red;
  margin-top: 10px;
}

</style>
