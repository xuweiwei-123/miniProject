<template>
  <div class="show" style="height: calc(100vh - 100px);background-color: honeydew;text-align: center;">
    <h2 style="font-size: 30px;text-align: center;">收银员列表</h2>
    <div class="search">
      <input type="text" v-model="keyword" placeholder="请输入关键字" />
      <button @click="search">搜索</button>
    </div>
    <table>
      <tr>
        <th>ID</th>
        <th>姓名</th>
        <th>性别</th>
        <th>年龄</th>
        <th>操作</th>
      </tr>
      <tr v-for="cashier in filteredCashiers" :key="cashier.id">
        <td>{{ cashier.id }}</td>
        <td>{{ cashier.name }}</td>
        <td>{{ cashier.sex }}</td>
        <td>{{ cashier.age }}</td>
        <td>
          <button @click="deleteCashier(cashier.id)">删除</button>
        </td>
      </tr>
    </table>
    <div class="page">
      <button @click="previousPage" :disabled="currentPage === 1">上一页</button>
      <span>{{ currentPage }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      currentPage: 1,
      pageSize: 3,
      totalPages: 0,
      showCashiers: [], // 原始收银员数据
      filteredCashiers: [], // 根据搜索过滤后的收银员数据
      keyword: '', // 搜索关键字
      selectedCashierId: null, // 选中的收银员ID
    };
  },
  mounted() {
    this.fetchCashiers();
  },
  methods: {
    fetchCashiers() {
      const url = `http://localhost:8080/supermarket/UserShowServlet?page=${this.currentPage}&pageSize=${this.pageSize}&keyword=${this.keyword}`;
      axios
        .get(url)
        .then(response => {
          const data = response.data;
          console.log(data); // 输出后端返回的数据

          if (Object.prototype.hasOwnProperty.call(data, 'data') && Object.prototype.hasOwnProperty.call(data, 'totalPages')) {
            this.totalPages = data.totalPages; // 更新总页数
            this.showCashiers = data.data.map(cashier => ({
              id: cashier.addUserID,
              name: cashier.addUserName,
              sex: cashier.addUserSex,
              age: cashier.addUserAge,
            }));
            this.filterCashiers(); // 初始收银员数据过滤
          } else {
            console.error('获取收银员信息失败：数据格式不正确');
            this.showCashiers = []; // 重置收银员数据
            this.filteredCashiers = []; // 重置过滤后的收银员数据
          }
        })
        .catch(error => {
          console.error('获取收银员信息失败：', error);
          this.showCashiers = []; // 重置收银员数据
          this.filteredCashiers = []; // 重置过滤后的收银员数据
        });
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchCashiers(); // 获取上一页收银员数据
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchCashiers(); // 获取下一页收银员数据
      }
    },
    search() {
      this.currentPage = 1; // 搜索时重置当前页为第一页
      this.fetchCashiers(); // 执行搜索获取收银员数据
    },
    filterCashiers() {
      this.filteredCashiers = this.showCashiers.filter(cashier => {
        const keywordLower = this.keyword.toLowerCase();
        const idMatch = String(cashier.id).includes(keywordLower);
        const nameMatch = cashier.name.toLowerCase().includes(keywordLower);
        return idMatch || nameMatch;
      });
    },
    deleteCashier(cashierId) {
      // 在这里实现删除收银员的逻辑
      this.selectedCashierId = cashierId; // 设置选中的会员ID
      // 发送删除请求到后端，请求成功后重新获取会员列表数据
      axios
        .delete(`http://localhost:8080/supermarket/DeleteCashierServlet?cashierId=${this.selectedCashierId}`)
        .then(() => {
          // 删除成功后重新获取收银员列表数据
          this.fetchCashiers();
          this.selectedCashierId = null; // 重置选中的收银员ID
          alert("删除收银员成功!")
        })
        .catch(error => {
          console.error('删除收银员失败：', error);
          this.selectedCashierId = null; // 重置选中的收银员ID
        });
      console.log(`删除收银员 ${cashierId}`);
    },
  },
};
</script>

<style>
h2 {
  font-size: 20px;
  margin-bottom: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  font-weight: bold;
}

button {
  padding: 4px 8px;
  margin-right: 4px;
}

.page {
  margin-top: 10px;
}
</style>
