<template>
  <div class="cashier-interface" style="background-color: lavenderblush;">
    <h2 style="font-size: 34px;text-align: center;">收  银  系  统</h2>

    <div class="product-selection module">
      <h3 style="font-size: 30px;text-align: center;">商  品  选  择</h3>
      <div class="search">
        <input type="text" v-model="searchQuery" placeholder="输入商品ID或名称" />
        <button @click="searchProducts">搜索</button>
      </div>
      <table>
        <thead>
          <tr>
            <th>商品ID</th>
            <th>商品名称</th>
            <th>商品种类</th>
            <th>商品单价</th>
            <th>商品数量</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(product) in displayedProducts" :key="product.id">
            <td>{{ product.id }}</td>
            <td>{{ product.name }}</td>
            <td>{{ product.type }}</td>
            <td>{{ product.price }}</td>
            <td>{{ product.number }}</td>
            <td><button @click="addToCart(product)">添加</button></td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button @click="prevPage">上一页</button>
        <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
        <button @click="nextPage">下一页</button>
      </div>
    </div>

    <div class="cart module">
      <h3 style="font-size: 30px;text-align: center;">收  银  列  表</h3>
      <table>
        <thead>
          <tr>
            <th>商品ID</th>
            <th>商品名称</th>
            <th>商品种类</th>
            <th>数量</th>
            <th>单价</th>
            <th>小计</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in cartItems" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.type }}</td>
            <td>{{ item.quantity }}</td>
            <td>{{ item.price }}</td>
            
            <td>{{ item.subtotal }}</td>
            <td><button @click="removeFromCart(item)">移除</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="total module">
      <span>总计： {{ total }}</span>
    </div>
    <div class="member module">
      <input type="text" v-model="memberId" placeholder="会员ID" />
      <button @click="checkMembership">验证会员/添加会员</button>
    </div>

    <div class="payment-options module">
      <button @click="selectPayment('现金')">现金</button>
      <button @click="selectPayment('信用卡')">信用卡</button>
      <button @click="selectPayment('移动支付')">移动支付</button>
    </div>

    <div class="payment-input module">
      <label for="payment-amount">输入支付金额：</label>
      <input id="payment-amount" type="number" v-model="paymentAmount" />
    </div>

    <div class="change module">
      <span>找零： {{ change }}</span>
    </div>

    <button @click="handleCheckout" style="color: red;font-size: larger;">结账</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      products: [],
      cartItems: [],
      total: 0,
      selectedPayment: '',
      paymentAmount: 0,
      searchQuery: '',
      memberId: '',
      currentPage: 1,
      rowsPerPage: 3,
      originalProducts: [], // 添加一个备份的商品列表
    };
  },

  mounted() {
  this.fetchProducts();
  const storedCartItems = localStorage.getItem('cartItems');    //验证收银列表是否存储，并显示
  if (storedCartItems) {
    this.cartItems = JSON.parse(storedCartItems);
    this.calculateTotal();
  }
},

  computed: {
    change() {
      return this.paymentAmount - this.total;
    },
    displayedProducts() {
      const start = (this.currentPage - 1) * this.rowsPerPage;
      const end = start + this.rowsPerPage;
      return this.products.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.products.length / this.rowsPerPage);
    },
  },

  methods: {
    searchProducts() {
      if (this.searchQuery.trim() === '') {
        // 如果搜索条件为空，则显示备份的完整商品列表
        this.products = [...this.originalProducts];
      } else {
        const query = this.searchQuery.toLowerCase().trim();
        this.products = this.originalProducts.filter(
          product =>
            product.id.toString().includes(query) ||
            product.name.toLowerCase().includes(query)
        );
      }
    },
    async fetchProducts() {
      try {
        const response = await axios.get('http://localhost:8080/supermarket/GoodsCashServlet');
        this.products = response.data.map(product => {
          return {
            id: product.addGoodsID,
            name: product.addGoodsName,
            type: product.addGoodsType,
            price: product.addGoodsPrice,
            number: product.addGoodsNumber,
            quantity: 0,
            subtotal: 0,
          };
        });
        this.originalProducts = [...this.products]; // 备份完整商品列表
      } catch (error) {
        console.error(error);
      }
    },

    addToCart(product) {
      const existingItem = this.cartItems.find(item => item.id === product.id);
      if (existingItem) {
        existingItem.quantity++;
        existingItem.subtotal = existingItem.quantity * existingItem.price;
        this.productName = existingItem.name; // 获取特定商品的名称
      } else {
        const newItem = {
          id: product.id,
          name: product.name,
          type: product.type,
          quantity: 1,
          price: product.price,
          subtotal: product.price,
        };
        this.cartItems.push(newItem);
        this.productName = newItem.name; // 获取特定商品的名称
      }
      this.calculateTotal();
    },
    removeFromCart(item) {
      const index = this.cartItems.indexOf(item);
      if (index > -1) {
        this.cartItems.splice(index, 1);
        this.calculateTotal();
      }
    },
    calculateTotal() {
      this.total = this.cartItems.reduce((sum, item) => sum + item.subtotal, 0);
    },
    selectPayment(paymentOption) {
      this.selectedPayment = paymentOption;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    checkMembership() {
      axios.get('http://localhost:8080/supermarket/CheckMembershipServlet', {
        params: {
          memberId: this.memberId
        }
      })
      .then(response => {
        if (response.data.isMember) {
          this.applyDiscount(0.9);
          alert("验证会员成功！");
        } else {
          if (this.total >= 200) {
            this.addMember();
            this.applyDiscount(0.9);
          }
        }
      })
      .catch(error => {
        console.error(error);
      });
    },

    applyDiscount(discount) {
      this.cartItems.forEach(item => {
        item.price *= discount;
        item.subtotal = item.price * item.quantity;
      });
      this.calculateTotal();
    },

    addMember() {
      localStorage.setItem('cartItems', JSON.stringify(this.cartItems));//保存此时收银列表的信息
      this.$router.push("MembershipTest")
},

    handleCheckout() {
  if (this.cartItems.length === 0) {
    alert('请先选择商品');
    return;
  }

  if (this.paymentAmount === 0) {
    alert('请输入支付金额');
    return;
  }

  if (this.total > this.paymentAmount) {
    alert('支付金额不足');
    return;
  }

  if (this.memberId === '') {
    alert('请输入会员ID');
    return;
  }

  axios
    .get('http://localhost:8080/supermarket/CheckMembershipServlet', {
      params: {
        memberId: this.memberId
      }
    })
    .then(response => {
      if (response.data.isMember) {
        // 会员验证成功，保存消费记录
        this.saveConsumptionRecord();
      } else {
        this.updateProductQuantities(); // 调用更新商品数量的方法

        // 非会员消费不满200元，不保存消费记录
        alert('非会员消费不满200元，不保存消费记录');
        this.cartItems = [];
        this.paymentAmount = 0; // 重置支付金额
        this.total = 0; // 清零总计
        this.change = 0; // 清零找零
        this.memberId = ''; // 清空会员ID
      }
    })
    .catch(error => {
      console.error(error);
    });
},
saveConsumptionRecord() {
  axios
    .post('http://localhost:8080/supermarket/GoodsConsumerServlet', {
      memberId: this.memberId,
      paymentOption: this.selectedPayment,
      paymentAmount: this.paymentAmount,
      cart: this.productName,
    })
    .then(() => {
      alert('消费记录保存成功');
      this.updateProductQuantities(); // 调用更新商品数量的方法
      this.cartItems = [];
      this.paymentAmount = 0; // 重置支付金额
      this.total = 0; // 清零总计
      this.change = 0; // 清零找零
      this.memberId = ''; // 清空会员ID
    })
    .catch((error) => {
      console.error(error);
    });
    localStorage.removeItem('cartItems');
},
updateProductQuantities() {
  this.cartItems.forEach(item => {
    const productId = item.id;
    const quantity = item.quantity;
    console.log("Quantity:", quantity); // 调试输出
    console.log("productId:", productId, typeof productId);
console.log("quantity:", quantity, typeof quantity);

    // 额外的检查
    if (!Number.isInteger(quantity) || quantity <= 0) {
      console.error("Invalid quantity:", quantity);
      return;
    }

    this.updateProductQuantity(productId, quantity);
  });
},

updateProductQuantity(productId, quantity) { // 修改此处的参数名为 quantity
  axios
    .post('http://localhost:8080/supermarket/GoodsUpdateServlet', {
      productId: productId,
      quantity: quantity // 修改此处的属性名为 quantity
      
    })
    .then(response => {
      console.log(response.data);
      alert("商品数量更新成功！");
    })
    .catch(error => {
      console.error(error);
      alert("商品数量更新失败！");
    });
},
  },
};
</script>

<style scoped>
.cashier-interface {
  font-family: Arial, sans-serif;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.total {
  text-align: right;
  margin-top: 20px;
}

.payment-options {
  margin-top: 20px;
}

.payment-options button {
  margin-right: 10px;
}

.payment-input {
  margin-top: 20px;
}

.change {
  text-align: right;
  margin-top: 20px;
}

.checkout-btn {
  display: block;
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.pagination {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination button {
  margin: 0 5px;
}
.module {
  margin-bottom: 20px; /* 设置模块的下方间距 */
}
</style>
