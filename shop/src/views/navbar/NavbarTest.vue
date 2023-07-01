  <template>
    <div class="header">
      <h1>超 市 收 银 系 统</h1>
    </div>
    <div class="container">
      <div class="sidebar">
      <ul>
        <li>
          <router-link to="/DashboardTest">仪表盘</router-link>
        </li>
        <li>
          <a href="#" @click="toggleMenu('products')">商品管理</a>
          <ul v-if="showMenu.products">
            <li>
              <router-link to="/ProductsTest">添加商品</router-link>
            </li>
            <li>
              <router-link to="/CategoriesTest">分类管理</router-link>
            </li>
          </ul>
        </li>
        <li>
          <router-link to="/OrdersTest">订单管理</router-link>
        </li>
        <li>
          <a href="#" @click="toggleMenu('members')">客户管理</a>
          <ul v-if="showMenu.members">
            <li>
              <router-link to="/MemberTest">客户列表</router-link>
            </li>
            <li>
              <router-link to="/MembershipTest">添加会员</router-link>
            </li>
          </ul>
        </li>
        <li>
          <a href="#" @click="toggleMenu('settings')">设置管理</a>
          <ul v-if="showMenu.settings">
            <li>
              <router-link to="/ChangePassword">修改密码</router-link>
            </li>
            <li>
              <router-link to="/AddCashier">添加收银员</router-link>
            </li>
            <li>
              <router-link to="/ShowCashier">收银员列表</router-link>
            </li>
          </ul>
        </li>
      </ul>
      </div>
      <div class="content">
        <router-view></router-view>
      </div>
    </div>
  </template>
<script>
   export default{
    data() {
    return {
      showMenu: {
        products: false,
        orders: false,
        members: false,
        settings: false,
      },
    };
  },
  methods: {
    toggleMenu(menu) {
      this.showMenu[menu] = !this.showMenu[menu];
    },
  },
   };
</script>
  <style>
  *{
    padding: 0;
    margin: 0;
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
  .container {
    display: flex;
    height: calc(100vh - 100px); /* 计算内容区域高度 */
  }
  
  .sidebar {
  background-color: #f0f0f0;
  padding: 10px;
  width: 200px; /* 调整导航栏的宽度 */
}

.sidebar ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.sidebar > ul > li {
  margin-bottom: 50px;
  font-size: x-large;
}

.sidebar > ul > li > a {
  display: block;
  height: 40px; /* 调整每个菜单项的高度 */
  line-height: 50px;
  padding: 0 10px;
  text-decoration: none;
  color: #333;
}

.sidebar > ul > li:hover > a {
  background-color: #ddd;
}

.sidebar > ul > li > ul {
  display: none;
  margin-top: 10px;
}

.sidebar > ul > li:hover > ul {
  display: block;
  padding-left: 10px;
}

.sidebar > ul > li > ul > li {
  margin-bottom: 5px;
}

.sidebar > ul > li > ul > li > a {
  display: block;
  height: 40px; /* 调整下拉菜单项的高度 */
  line-height: 40px;
  padding: 0 10px;
  text-decoration: none;
  color: #333;
}

.sidebar > ul > li > ul > li > a:hover {
  background-color: #ddd;
}
.content{
  flex-grow: 1;
}
  </style>
  