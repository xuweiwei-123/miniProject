<template>
  <div style="height: calc(100vh - 100px);background-color: honeydew;">
    <h2 style="text-align: center;font-size: 34px;">仪表盘</h2>
    <div style="color: darkorchid;font-size: 28px;">
      当天收入总额：{{ totalExpense }}
    </div>
    <div>
      <div>
        <h3 style="text-align: center;font-size: 24px;">种类列表</h3>
        <table>
          <thead>
            <tr>
              <th>商品种类</th>
              <th>数量</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in products" :key="product.name" :class="{ 'red': product.quantity < 20 }">
              <td>{{ product.name }}</td>
              <td>{{ product.quantity }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div>
      <div>
        <h3 style="text-align: center;font-size: 24px;">消费记录</h3>
        <table>
          <thead>
            <tr>
              <th>商品种类</th>
              <th>数量</th>
              <th>金额</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="record in expenseRecords" :key="record.id">
              <td>{{ record.type }}</td>
              <td>{{ record.quantity }}</td>
              <td>{{ record.amount }}</td>
            </tr>
          </tbody>
        </table>
      </div>

    </div>
    <div>
      <button @click="fetchProducts()">刷新</button>
      <button @click="fetchExpenseRecords()">刷新消费记录</button>
      <button @click="exportExpenseRecords()">导出消费记录</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      products: [],
      totalExpense: null,
      expenseRecords: [], // 新添加的消费记录数组
    };
  },
  mounted() {
    this.fetchProducts();
    this.fetchTotalExpense();
    this.fetchExpenseRecords(); // 在mounted钩子中调用获取消费记录的方法
  },
  methods: {
    fetchProducts() {
      axios.post('http://localhost:8080/supermarket/GetTypesServlet')
        .then(response => {
          this.products = response.data.data;
          console.log(response.data.data);
        })
        .catch(error => {
          console.error(error);
        });
    },
    fetchTotalExpense() {
      axios.post('http://localhost:8080/supermarket/GetConsumeServlet')
        .then(response => {
          this.totalExpense = response.data.total;
        })
        .catch(error => {
          console.error(error);
        });
    },
    fetchExpenseRecords() {
      axios.post('http://localhost:8080/supermarket/GetConsumeServlet')
        .then(response => {
          this.expenseRecords = response.data.records;
          console.log(response.data.records);
        })
        .catch(error => {
          console.error(error);
        });
    },
    exportExpenseRecords() {
  axios
    .post('http://localhost:8080/supermarket/ExportExpenseRecordsServlet', {
      records: this.expenseRecords,
    }, {
      responseType: 'blob' // 设置响应类型为 blob
    })
    .then(response => {
      // 创建一个下载链接
      const downloadLink = document.createElement('a');
      const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' }); // 修改 MIME 类型为对应的 Excel 类型
      downloadLink.href = window.URL.createObjectURL(blob);
      downloadLink.setAttribute('download', 'expense_records.xlsx'); // 修改文件名和扩展名为对应的 Excel 文件名和扩展名
      document.body.appendChild(downloadLink);

      // 模拟点击下载链接
      downloadLink.click();

      // 清理下载链接
      document.body.removeChild(downloadLink);

      // 处理导出成功的逻辑，例如提示用户下载链接或其他操作
      console.log('导出成功');
    })
    .catch(error => {
      // 处理导出失败的逻辑
      console.error('导出失败', error);
    });
}



  },
};
</script>

<style>
h2 {
  color: #333;
  font-size: 24px;
  margin-bottom: 10px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

th,
td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.red {
  background-color: red;
  color: white;
}
</style>
