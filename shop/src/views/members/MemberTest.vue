<template>
  <div class="show" style="height: calc(100vh - 100px);background-color: honeydew;">
    <h2 style="font-size: 30px;text-align: center;">会员列表</h2>
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
      <tr v-for="member in filteredMembers" :key="member.id">
        <td>{{ member.id }}</td>
        <td>{{ member.name }}</td>
        <td>{{ member.sex }}</td>
        <td>{{ member.age }}</td>
        <td>
          <button @click="deleteMember(member.id)">删除</button>
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
      members: [], // 原始会员数据
      filteredMembers: [], // 根据搜索过滤后的会员数据
      keyword: '', // 搜索关键字
      selectedMemberId: null, // 选中的会员ID
    };
  },
  mounted() {
    this.fetchMembers();
  },
  methods: {
    fetchMembers() {
      const url = `http://localhost:8080/supermarket/MemberShowServlet?page=${this.currentPage}&pageSize=${this.pageSize}&keyword=${this.keyword}`;
      axios
        .get(url)
        .then(response => {
          const data = response.data;
          console.log(data); // 输出后端返回的数据

          if (Object.prototype.hasOwnProperty.call(data, 'data') && Object.prototype.hasOwnProperty.call(data, 'totalPages')) {
            this.totalPages = data.totalPages; // 更新总页数
            this.members = data.data.map(member => ({
              id: member.addMemberID,
              name: member.addMemberName,
              sex: member.addMemberSex,
              age: member.addMemberAge,
            }));
            this.filterMembers(); // 初始会员数据过滤
          } else {
            console.error('获取会员信息失败：数据格式不正确');
            this.members = []; // 重置会员数据
            this.filteredMembers = []; // 重置过滤后的会员数据
          }
        })
        .catch(error => {
          console.error('获取会员信息失败：', error);
          this.members = []; // 重置会员数据
          this.filteredMembers = []; // 重置过滤后的会员数据
        });
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchMembers(); // 分页切换时重新获取会员数据
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchMembers(); // 分页切换时重新获取会员数据
      }
    },
    search() {
      this.currentPage = 1; // 重置当前页码为第一页
      this.fetchMembers(); // 执行搜索
    },
    filterMembers() {
      const searchKeyword = this.keyword.toLowerCase(); // 将搜索关键字转换为小写，实现不区分大小写匹配
      this.filteredMembers = this.members.filter(member => {
        const idMatch = member.id.toLowerCase().includes(searchKeyword); // 检查ID是否与搜索关键字匹配
        const nameMatch = member.name.toLowerCase().includes(searchKeyword); // 检查姓名是否与搜索关键字匹配
        return idMatch || nameMatch; // 如果ID或姓名匹配，返回true
      });
    },
    deleteMember(memberId) {
      this.selectedMemberId = memberId; // 设置选中的会员ID
      // 发送删除请求到后端，请求成功后重新获取会员列表数据
      axios
        .delete(`http://localhost:8080/supermarket/DeleteMemberServlet?memberId=${this.selectedMemberId}`)
        .then(() => {
          // 删除成功后重新获取会员列表数据
          this.fetchMembers();
          this.selectedMemberId = null; // 重置选中的会员ID
          alert("删除会员成功!")
        })
        .catch(error => {
          console.error('删除会员失败：', error);
          this.selectedMemberId = null; // 重置选中的会员ID
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
