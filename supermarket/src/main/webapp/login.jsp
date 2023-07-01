<%--
  Created by IntelliJ IDEA.
  User: 86193
  Date: 2023/6/8
  Time: 9:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录界面</title>
    <style>
      .container{
        width: 500px;
        height: 400px;
        margin-left: 300px;
        margin-top: 260px;
      }
      .frame{
        width: 360px;
        margin-left: 100px;
        margin-top: 45px;
        color: grey;
        font-size: 25px;
      }
      input{
        width: 228px;
        height: 30px;
      }
    </style>
</head>
<body>
<div class="container" style="border: grey 2px solid">
  <h1 style="margin-top: 40px;margin-left: 130px">超&nbsp;市&nbsp;收&nbsp;银&nbsp;系&nbsp;统</h1>
  <div class="frame" style="font-size: 30px">
    <form action="userLoginServlet" method="post" onclick="return Login()">
      用户：<input type="text" style="font-size: 28px" id="userName" name="userName" class="myText"><br><br>
      密码：<input type="password" style="font-size: 28px" id="userPassword" name="userPassword" class="myText"><br><br>
      <input style="width: 140px;margin-left: 20px" type="submit" value="登录">
      <input style="width: 140px;margin-left: 20px" type="submit" value="注册"><br>
        <span style="color: red; font-size: 15px" id="tip"><%= request.getAttribute("tip") %></span>
    </form>
  </div>
</div>
</body>
</html>

<script>
  function Login(){
    let userName = document.getElementById("userName").value;
    let userPassword = document.getElementById("userPassword").value;
    if (userName == null || userName.trim() == ""){
      document.getElementById("tip").innerHTML = "账户不能为空";
      return false;
    }
    if (userPassword == null || userPassword.trim() == ""){
      document.getElementById("tip").innerHTML = "密码不能为空";
      return false;
    }
    return true;
  }
</script>
