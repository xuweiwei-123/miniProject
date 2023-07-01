// index.js
import { createRouter, createWebHistory } from 'vue-router';
import LoginTest from '@/views/LoginTest';
import DashboardTest from '@/views/dashboards/DashboardTest.vue';
import ProductsTest from '@/views/products/ProductsTest.vue';
import CategoriesTest from '@/views/products/CategoriesTest.vue';
import OrdersTest from '@/views/orders/OrdersTest.vue';
import MemberTest from '@/views/members/MemberTest.vue';
import MembershipTest from '@/views/members/MembershipTest.vue';
import ChangePassword from '@/views/setting/ChangePassword.vue';
import NavbarTest from '@/views/navbar/NavbarTest.vue';
import AddCashier from '@/views/setting/AddCashier.vue';
import ShowCashier from '@/views/setting/ShowCashier.vue';

const routes = [
  {
    path: '/',
    redirect: '/LoginTest'
  },
  {
    path: '/LoginTest',
    name: 'LoginTest',
    component: LoginTest
  },
  {
    path: '/NavbarTest',
    name: 'NavbarTest',
    component: NavbarTest,
    children:[
      {
        path: '/DashboardTest',
        name: 'DashboardTest',
        component: DashboardTest
      },
      {
        path: '/ProductsTest',
        name: 'ProductsTest',
        component: ProductsTest,
      },
      {
        path: '/CategoriesTest',
        name: 'CategoriesTest',
        component: CategoriesTest,
      },
      {
        path: '/OrdersTest',
        name: 'OrdersTest',
        component: OrdersTest
      },
      {
        path: '/MemberTest',
        name: 'MemberTest',
        component: MemberTest,
      },
      {
        path: '/MembershipTest',
        name: 'MembershipTest',
        component: MembershipTest,
      },
      {
        path: '/ChangePassword',
        name: 'ChangePassword',
        component: ChangePassword,
      },
      {
        path: '/AddCashier',
        name: 'AddCashier',
        component: AddCashier,
      },
      {
        path: '/ShowCashier',
        name: 'ShowCashier',
        component: ShowCashier,
      }
    ]
  },
  // 其他路由...
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
