<template>
  <div class="add-content">
    <h2 style="font-size: 30px;">添  加  会  员</h2>
    <div class="add">
      <form @submit.prevent="addCashier">
        <label class="add-label">会员ID：</label>
        <input type="text" id="addMemberID" class="set-input" v-model="addMemberID" required>
        <label class="add-label">会员名：</label>
        <input type="text" id="addMemberName" class="set-input" v-model="addMemberName" required>
        <label class="add-label">密码：</label>
        <input type="text" id="addMemberPassword" class="set-input" v-model="addMemberPassword" required>
        <label class="add-label">性别：</label>
        <input type="text" id="addMemberSex" class="set-input" v-model="addMemberSex" required>
        <label class="add-label">年龄：</label>
        <input type="text" id="addMemberAge" class="set-input" v-model="addMemberAge" required>
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
      addMemberID: '',
      addMemberName: '',
      addMemberPassword: '',
      addMemberSex: '',
      addMemberAge: '',
      error: ''
    };
  },
  methods: {
    addCashier(){
      //发送添加收银员请求到后端
      axios.post('http://localhost:8080/supermarket/MemberRegisterServlet',{
        addMemberID: this.addMemberID,
        addMemberName: this.addMemberName,
        addMemberPassword: this.addMemberPassword,
        addMemberSex: this.addMemberSex,
        addMemberAge: this.addMemberAge
      })
      .then(response =>{
        if(response.data.success){
          //添加成功
          this.addMemberID = '';
          this.addMemberName = '';
          this.addMemberPassword = '';
          this.addMemberSex = '';
          this.addMemberAge = '';
          alert("成功添加会员");
          this.$router.push('/OrdersTest'); // 返回收银界面
        }else{
          //添加失败，显示错误信息
          this.error = response
          .data.message;
          console.log("Error:", this.error);
        }
      })
      .catch(error => {
        console.error(error);
        this.error = '会员添加失败';
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
