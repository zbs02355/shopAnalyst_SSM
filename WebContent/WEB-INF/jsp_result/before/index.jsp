<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 设置文件路径 -->
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>首页</title>
	
	<!-- CSS文件 -->
	<link href="<%=basePath%>CSS/indexCSS.css" rel="stylesheet" type="text/css"/>

</head>
<body>
	<div id="title">
		<p>欢迎进入川渝销售数据可视化分析系统</p>
		<form action="${pageContext.request.contextPath}/index/comein">
			<input id="sureButton" type="submit" value="点击进入"/>
		</form>
	</div>
	
</body>
</html>