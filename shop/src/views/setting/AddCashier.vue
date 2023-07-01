<template>
    <div class="add-content">
      <h2 style="font-size: 30px;">添  加  收  银  员</h2>
      <div class="add">
        <form @submit.prevent="addCashier">
          <label class="add-label">用户ID：</label>
          <input type="text" id="addUserID" class="set-input" v-model="addUserID" required>
          <label class="add-label">用户名：</label>
          <input type="text" id="addUserName" class="set-input" v-model="addUserName" required>
          <label class="add-label">密码：</label>
          <input type="text" id="addUserPassword" class="set-input" v-model="addUserPassword" required>
          <label class="add-label">性别：</label>
          <input type="text" id="addUserSex" class="set-input" v-model="addUserSex" required>
          <label class="add-label">年龄：</label>
          <input type="text" id="addUserAge" class="set-input" v-model="addUserAge" required>
          <button type="submit" class="add-button">确认添加</button>
        </form>
      </div>
      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </template>
  
  <script>
import axios from 'axios';

  export default {
    data() {
      return {
        addUserID: '',
        addUserName: '',
        addUserPassword: '',
        addUserSex: '',
        addUserAge: '',
        error: ''
      };
    },
    methods: {
      addCashier(){
        //发送添加收银员请求到后端
        axios.post('http://localhost:8080/supermarket/UserRegisterServlet',{
          addUserID: this.addUserID,
          addUserName: this.addUserName,
          addUserPassword: this.addUserPassword,
          addUserSex: this.addUserSex,
          addUserAge: this.addUserAge
        })
        .then(response =>{
          if(response.data.success){
            //添加成功
            this.addUserID = '';
            this.addUserName = '';
            this.addUserPassword = '';
            this.addUserSex = '';
            this.addUserAge = '';
            alert("成功添加收银员");
          }else{
            //添加失败，显示错误信息
            this.error = response
            .data.message;
            console.log("Error:", this.error);
          }
        })
        .catch(error => {
          console.error(error);
          this.error = '收银员添加失败';
        });
      }
    }
  };
  </script>
  
  <style>
  .add-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 100px);
  background-color: azure;
}

h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.add {
  width: 300px;
}

.add-label {
  display: block;
  font-weight: bold;
  margin-bottom: 10px;
}

.add-input {
  width: 100%;
  padding: 5px;
  margin-bottom: 10px;
}

.add-button {
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
  