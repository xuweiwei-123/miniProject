<template>
    <div class="login-container">
      <header class="header">
      <h1>超 市 收 银 系 统</h1>
    </header>
    <div class="login-form">
      <h2>登录</h2>
      <form>
        <label for="username">用户：</label>
        <input type="text" id="username" v-model="username" required>
        <br><br>
        <label for="password">密码：</label>
        <input type="password" id="password" v-model="password" required>
        <br><br>
        <button type="submit" @click.prevent="loginForm">登录</button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
    </div>
    </div>
  </template>
  
  <script>
import axios from 'axios';
  /* eslint-disable vue/multi-word-component-names */
  export default {
    data() {
      return {
        username: '',
        password: '',
        error: ''
      };
    },
    methods: {
      loginForm() {
  // 发送登录请求到后端
  console.log("Sending login request");
  console.log("Username:", this.username);
  console.log("Password:", this.password);

  axios.post('http://localhost:8080/supermarket/UserLoginServlet', {
    username: this.username,
    password: this.password
  })
    .then(response => {
      console.log("Login response:", response.data);
      if (response.data.success) {
        // 登录成功，处理相应逻辑
        this.$router.push('/dashboardTest');
      } else {
        // 登录失败，显示错误消息
        this.error = response.data.message;
        console.log("Error:", this.error);
      }
    })
    .catch(error => {
      console.error(error);
    });
    // 进行用户身份验证的逻辑
    /*if (!this.username || !this.password) {
      this.error = '用户名或密码不能为空';
      return;
    }
    if (this.username === 'admin' && this.password === 'password') {
    // 登录成功，跳转到NavbarTest页面
    this.$router.push('/NavbarTest');
    console.log("跳转成功");
    } else {
    // 登录失败，显示错误消息
    this.error = '用户名或密码错误';
  }*/
}
    }
  };
  
  </script>
  
  <style>
  body, html {
  margin: 0;
  padding: 0;
  }
  .login-container {
  display: grid;
  place-items: center;
  height: 100vh;
  background-color: #E6D9F2; /* 淡紫色 */
}
  .header{
    width: 100%;
    height: 100px;
    font-size: xx-large;
    font-family: "华文琥珀";
    color: rgb(248, 159, 174);
    background-color: rgb(249, 229, 202);
    text-align: center;
  }
  h1{
    line-height: 100px;
  }
.login-form {
  text-align: center;
  padding: 20px;
  border: 1px solid #B2E6D4; /* 淡绿色 */
  border-radius: 5px;
  background-color: #FFF; /* 白色 */
}
  .error {
    color: red;
  }
  </style>
