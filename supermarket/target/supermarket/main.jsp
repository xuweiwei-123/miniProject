<%--
  Created by IntelliJ IDEA.
  User: 86193
  Date: 2023/6/8
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        .header{
            background-color: #a6c1ee;
            color: white;
            width: 100%;
            height: 120px;
            text-align: center;
            line-height: 120px;
        }
        .daohang{
            width: 230px;
            height: 385px;
            border: grey 1px solid;
        }
        .file{
            width: 200px;
            height: 50px;
            margin-top: 11px;
            margin-left: 10px;
            border: grey 1px solid;
            background-color: #abc1ee;
        }
        a{
            color: white;
            font-size: 26px;
            margin-left: 25px;
            margin-top: 25px;
        }
    </style>
</head>
<body>
<div class="header" style="font-size: xxx-large">超市收银系统</div>
<br><br>
<div class="daohang">
    <!--<div id="file" onclick="clickThis()"></div>-->
    <li class="file"><a href="#">查看首页</a></li>
    <li class="file"><a href="#">收银界面</a></li>
    <li class="file"><a href="#">商品界面</a></li>
    <li class="file"><a href="#">会员管理</a></li>
    <li class="file"><a href="#">员工管理</a></li>
    <li class="file"><a href="#">退出系统</a></li>
</div>
    <% if (request.getAttribute("mainRight") == null)
    { request.getRequestDispatcher("blank.jsp").include(request, response); }
    else { request.getRequestDispatcher((String) request.getAttribute("mainRight")).include(request, response); } %>
</body>
</html>
