<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- 设置文件路径 -->
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-- http://localhost:8090/bigDataAnalyst -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height: 100%">
<head>
<!-- 引用基础路径 -->
<!-- <base href="<%=basePath%>"> -->

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 

	<title>首页</title>
	
	<!-- JavaScript脚本文件 -->
	<script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-gl/echarts-gl.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-stat/ecStat.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
       <script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=v46Mm4MHOf4bBy2TQDGv2htREnDK2N8U"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/simplex.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/jquery-3.4.1.min.js"></script>
</head>

<body style="height: 100%; margin: 0">
	<!-- 地图界面 -->
	<div id="container" style="height: 95%"></div>
	
	<!-- 时间工具 -->
	<div id="toolkit" style="height: 5%">
		<form:form modelAttribute="userChoose" method="post" action="${pageContext.request.contextPath}/index/userChoose">
				<form:select path="month">
					<option />请选择月份：
					<form:options items="${months }"></form:options>
				</form:select>
				<form:select id="selectedCityOrCounty" path="cityOrCounty">
					<option />请选择市/县：
					<form:option value="市"></form:option>
					<form:option value="县"></form:option>
				</form:select>
				<form:select id="selectedDataCategory" path="dataCategory">
					<option />请选择要展示的数据：
					<form:option value="查看不同市县购买力活跃程度"></form:option>
					<form:option value="查看不同市县销量最高的商品类型"></form:option>
				</form:select>
				<form:button value="提交">提交</form:button>
		</form:form>
	</div>
	
	<!-- echarts脚本 -->
	<script type="text/javascript" >
	//获取地图展示容器
	var dom = document.getElementById("container");

	//初始化Echarts
	var myChart = echarts.init(dom);	
	var app = {};
	option = null;

	//初始数据
	var data = [
		{name: '成都', value: 1053}
	];
	var geoCoordMap = {
		'成都':[103.9719585058,30.7576026341]
	};

	//存放后台返回的数据
	var selectedData = new Array();
	
	
	//创建变量用于获取用户选择的表单值
	//var selectedCC = document.getElementById("selectedCityOrCounty");
	//var selectedDC = document.getElementById("selectedDataCategory");

	//合并原始数据
	var convertData = function (data) {
	    var res = [];
	    for (var i = 0; i < data.length; i++) {
	        var geoCoord = geoCoordMap[data[i].name];
	        if (geoCoord) {
	            res.push({
	                name: data[i].name,
	                value: geoCoord.concat(data[i].value)
	            });
	        }
	    }
	    return res;
	};

	//创建Echarts选项
	option = {
		    title: {
		    	text: "川渝城市销售数据分析系统",
				subtext: 'data from Mrs.LuoFei',
		        left: 'center'
		    },
			//悬浮框
		    tooltip : {
		        trigger: 'item'
		    },
		    bmap: {
		        center: [104.2008214537,30.4287252658],
		        zoom: 7,
		        roam: true,
		    },
		    series : [
		        {
		            name: 'shopNum',
		            type: 'scatter',
		            coordinateSystem: 'bmap',
		            data: convertData(data),
		            symbolSize: function (val) {
		                return val[2] / 200;
		            },
		            label: {
		                normal: {
		                    formatter: '{b}',
		                    position: 'right',
		                    show: false
		                },
		                emphasis: {
		                    show: true
		                }
		            },
		            itemStyle: {
		                normal: {
		                    color: 'purple'
		                }
		            }
		        }
		    ]
		};	

	
	//初始化Echarts
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
	
	//购买力总数对应的Echarts设置
	function shopSumOption(myEcharts, cityOrTown) {
		var sizeLevel;
		if(cityOrTown == "city"){
			sizeLevel = 10;
		}
		else if(cityOrTown == "town"){
			sizeLevel = 0.1;
		}
		
		//设置Echarts选项
		myEcharts.setOption({
			title: {
		        text: '川渝城市销售数据分析系统',
		        subtext: 'data from Mrs.LuoFei',
		        left: 'center'
		    },
			// 悬浮框
		    tooltip : {
		        trigger: 'item'
		    },
		    series:[{
		    	name: 'Shop Num',
	            type: 'scatter',
	            coordinateSystem: 'bmap',
	            data: selectedData,
	            symbolSize: function (val, sizeLevel) {
	            	//console.log(val[2]);
	                return val[2] / 100;
	            },
	            label: {
	                normal: {
	                    formatter: '{b}',
	                    position: 'right',
	                    show: false
	                },
	                emphasis: {
	                    show: true
	                }
	            },
	            itemStyle: {
	                normal: {
	                    color: 'purple'
	                }
	            }
		    }]
		})
		
		//测试
		console.log("Echarts选项设置完成！");
	}
	
	
	
	//异步读取数据
	function readData(myEcharts){
		$.ajax({
			// 输入请求的路径
			url: "${pageContext.request.contextPath}/index/selectDataForShopSum",
			// 请求的类型
			type: "get",
			contentType: "application/json; charset=utf-8",
			// 返回的数据类型
			dataType: "json",
			success: function(data) {
				//测试
				console.log(data);
				console.log(data[0].selectedCC);
				
				// 创建一个数组作为option里的数据
				var optionData = new Array();
				
				if (data[0].selectedCC == "city") {
					//测试
					console.log("开始给数据赋值");
					
					// 遍历列表里的元素
					for (var i = 0; i < data.length; i++) {
						// 获取数据
						var num = data[i].num;
						var city = data[i].city;
						var longititude = data[i].longititude;
						var lantitude = data[i].lantitude;
						
						// 创建一个用于存放上述数据的对象
						var obj = new Object();
						
						// 创建一个用于存放经纬度、购买数量的数组
						var valueArray = new Array();
						valueArray[0] = parseFloat(longititude);
						valueArray[1] = parseFloat(lantitude);
						valueArray[2] = parseInt(num);
						
						// 为对象输入值
						obj.name = city;
						obj.value = valueArray;
						
						// 为数组输入值
						optionData[i] = obj;
					}
					selectedData = optionData;
					
					//调用函数shopSumOption进行Echarts选项设置
					shopSumOption(myEcharts, data[0].selectedCC);
					
					//测试
					console.log("赋值完成！");
					
				}
				else if (data[0].selectedCC == "town") {
					//测试
					console.log("开始给数据赋值");
					
					// 遍历列表里的元素
					for (var i = 0; i < data.length; i++) {
						// 获取数据
						var num = data[i].num;
						var town = data[i].town;
						var longititude = data[i].longititude;
						var lantitude = data[i].lantitude;
						
						// 创建一个用于存放上述数据的对象
						var obj = new Object();
						
						// 创建一个用于存放经纬度、购买数量的数组
						var valueArray = new Array();
						valueArray[0] = parseFloat(longititude);
						valueArray[1] = parseFloat(lantitude);
						valueArray[2] = parseInt(num);
						
						// 为对象输入值
						obj.name = town;
						obj.value = valueArray;
						
						// 为数组输入值
						optionData[i] = obj;
					}
					selectedData = optionData;
					//调用函数shopSumOption进行Echarts选项设置
					shopSumOption(myEcharts, data[0].selectedCC);
					
				}

			//success()	
			}
		})
	}

	//调用读取异步数据函数
	readData(myChart);
	</script>
	
</body>
</html>