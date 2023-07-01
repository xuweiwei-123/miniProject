<template>
  <div class="show" style="height: calc(100vh - 100px);background-color: honeydew;">
    <h2 style="font-size: 30px;text-align: center;">商品列表</h2>
    <div class="search">
      <input type="text" v-model="keyword" placeholder="请输入关键字" />
      <button @click="search">搜索</button>
    </div>
    <table>
      <tr>
        <th>商品ID</th>
        <th>商品名</th>
        <th>商品种类</th>
        <th>商品价格</th>
        <th>商品数量</th>
        <th>操作</th>
      </tr>
      <tr v-for="goods in filteredGoods" :key="goods.id">
        <td>{{ goods.id }}</td>
        <td>{{ goods.name }}</td>
        <td>{{ goods.type }}</td>
        <td>{{ goods.price }}</td>
        <td>{{ goods.number }}</td>
        <td>
          <button @click="deleteGoods(goods.id)">删除</button>
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
      pageSize: 5,
      totalPages: 0,
      goodss: [], // 原始商品数据
      filteredGoods: [], // 根据搜索过滤后的商品数据
      keyword: '', // 搜索关键字
      selectedGoodsId: null, // 选中的商品ID
    };
  },
  mounted() {
    this.fetchGoods();
  },
  methods: {
    fetchGoods() {
      const url = `http://localhost:8080/supermarket/GoodsShowServlet?page=${this.currentPage}&pageSize=${this.pageSize}&keyword=${this.keyword}`;
      axios
        .get(url)
        .then(response => {
          const data = response.data;
          console.log(data); // 输出后端返回的数据

          if (Object.prototype.hasOwnProperty.call(data, 'data') && Object.prototype.hasOwnProperty.call(data, 'totalPages')) {
            this.totalPages = data.totalPages; // 更新总页数
            this.goodss = data.data.map(goods => ({
              id: goods.addGoodsID,
              name: goods.addGoodsName,
              type: goods.addGoodsType,
              price: goods.addGoodsPrice,
              number: goods.addGoodsNumber,
            }));
            this.filterGoods(); // 初始商品数据过滤
          } else {
            console.error('获取商品信息失败：数据格式不正确');
            this.goodss = []; // 重置是商品数据
            this.filteredGoods = []; // 重置过滤后的商品数据
          }
        })
        .catch(error => {
          console.error('获取商品信息失败：', error);
          this.goodss = []; // 重置商品数据
          this.filteredGoods = []; // 重置过滤后的商品数据
        });
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchGoods(); // 分页切换时重新获取商品数据
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchGoods(); // 分页切换时重新获取商品数据
      }
    },
    search() {
      this.currentPage = 1; // 重置当前页码为第一页
      this.fetchGoods(); // 执行搜索
    },
    filterGoods() {
      const searchKeyword = this.keyword.toLowerCase(); // 将搜索关键字转换为小写，实现不区分大小写匹配
      this.filteredGoods = this.goodss.filter(goods => {
        const idMatch = goods.id.toLowerCase().includes(searchKeyword); // 检查ID是否与搜索关键字匹配
        const nameMatch = goods.name.toLowerCase().includes(searchKeyword); // 检查名字是否与搜索关键字匹配
        return idMatch || nameMatch; // 如果ID或名字匹配，返回true
      });
    },
    deleteGoods(goodsId) {
      this.selectedGoodsId = goodsId; // 设置选中的商品ID
      // 发送删除请求到后端，请求成功后重新获取商品列表数据
      axios
        .delete(`http://localhost:8080/supermarket/DeleteGoodsServlet?goodsId=${this.selectedGoodsId}`)
        .then(() => {
          // 删除成功后重新获取商品列表数据
          this.fetchGoods();
          this.selectedGoodsId = null; // 重置选中的商品ID
          alert("删除商品成功!")
        })
        .catch(error => {
          console.error('删除商品失败：', error);
          this.selectedGoodsId = null; // 重置选中的商品ID
        });
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

.search {
  margin-bottom: 10px;
}

.search input {
  padding: 4px;
  margin-right: 4px;
}

.search button {
  padding: 4px 8px;
}
</style>
