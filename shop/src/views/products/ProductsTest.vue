<template>
  <div class="add-content">
    <h2 style="font-size: 30px;">添  加  商  品</h2>
    <div class="add">
      <form @submit.prevent="addGoods">
        <label class="add-label">商品ID：</label>
        <input type="text" id="addGoodsID" class="set-input" v-model="addGoodsID" required>
        <label class="add-label">商品名：</label>
        <input type="text" id="addGoodsName" class="set-input" v-model="addGoodsName" required>
        <label class="add-label">商品所属种类：</label>
        <select id="addGoodsType" v-model="addGoodsType" class="set-input" required>
          <option value="" disabled selected>请选择商品种类</option>
          <option v-for="category in categories" :key="category" :value="category">{{ category }}</option>
        </select>
        <input type="text" id="addGoodsType" class="set-input" v-model="addGoodsType" required>
        <label class="add-label">商品价格：</label>
        <input type="text" id="addGoodsPrice" class="set-input" v-model="addGoodsPrice" required>
        <label class="add-label">商品数量：</label>
        <input type="text" id="addGoodsNumber" class="set-input" v-model="addGoodsNumber" required>
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
      addGoodsID: '',
      addGoodsName: '',
      addGoodsType: '',
      addGoodsPrice: '',
      addGoodsNumber: '',
      error: '',
      categories: ['水果','零食','蔬菜','肉类','饮料']
    };
  },
  methods: {
    addGoods(){
      //发送添加商品请求到后端
      axios.post('http://localhost:8080/supermarket/AddGoodsServlet',{
        addGoodsID: this.addGoodsID,
        addGoodsName: this.addGoodsName,
        addGoodsType: this.addGoodsType,
        addGoodsPrice: this.addGoodsPrice,
        addGoodsNumber: this.addGoodsNumber
      })
      .then(response =>{
        if(response.data.success){
          //添加成功
          this.addGoodsID = '';
          this.addGoodsName = '';
          this.addGoodsType = '';
          this.addGoodsPrice = '';
          this.addGoodsNumber = '';
          alert("成功添加商品");
        }else{
          //添加失败，显示错误信息
          this.error = response
          .data.message;
          console.log("Error:", this.error);
        }
      })
      .catch(error => {
        console.error(error);
        this.error = '商品添加失败';
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
