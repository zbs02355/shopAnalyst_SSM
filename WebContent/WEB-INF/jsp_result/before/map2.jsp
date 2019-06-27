<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- 设置文件路径 -->
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height: 100%">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
	<title>查看不同市县销量最高的商品类型</title>
	
	<!-- JS文件 -->
	<script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-gl/echarts-gl.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-stat/ecStat.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/simplex.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery-3.4.1.min.js"></script>
</head>
<body style="height: 100%; margin: 0">
	<!-- 地图界面 -->
	<div id="map" style="height: 95%"></div>
	
	<!-- 时间工具 -->	
	<div id="toolkit" style="height: 5%">
		<form:form modelAttribute="userChoose" method="post" action="${pageContext.request.contextPath}/index/userChooseInSecond">
				<form:select path="month">
					<option />请选择月份：
					<form:options items="${months }"></form:options>
				</form:select>
				<form:select id="selectedCityOrCounty" path="cityOrCounty">
					<option />请选择市/县：
					<form:option value="市"></form:option>
					<form:option value="县"></form:option>
				</form:select>
				<form:button value="提交">提交</form:button>
		</form:form>
	</div>
	
	<!-- echarts脚本 -->
	<script type="text/javascript">
		//获取地图容器
		var myChart = echarts.init(document.getElementById('map'));
	
		//城市坐标
		var geoCoordMap = {
        	
    	};
	
		//存放每个城市的商品种类的最大销量
		var rawData = [
        	
    	];
	
		//存放每种商品类型的颜色
		const categoryColor = {
			"baby care": "#DC143C",  //猩红
			"battery": "#32CD32",  //绿色
			"feminine care": "#FFFF00",  //黄色
			"hair care": "#FF4500",  //红色
			"laundry": "#808080",  //银色
			"oral care": "#A52A2A",  //棕色
			"pcc" : "#FFA500",  //橙色
			"shaving" : "#00FFFF", //青色
			"skin care(female)": "#2F4F4F", //石板灰
			"skin care(male)": "#7FFFAA"  //碧绿
		};
		
		//初始化中国地图
		option = {
		        animation: false,
		        // 地图背景颜色
		        backgroundColor: new echarts.graphic.RadialGradient(0.5, 0.5, 0.4, [{
		            offset: 0,
		            color: '#4b5769'
		        }, {
		            offset: 1,
		            color: '#404a59'
		        }]),
		        tooltip: {
		            trigger: 'axis'
		        },
		        geo: {
		            map: 'china',
		            // silent: true,
		            roam: true,
		            zoom: 6, // 地图初始大小
		            center: [103.5353076954, 31.7392021554], // 初始中心位置
		            label: {
		            	normal:{
		            		emphasis: {
		                        show: false,
		                        areaColor: '#eee'
		                    }
		            	},
		                emphasis: {
		                    show: false,
		                    areaColor: '#eee'
		                }
		            },
		            // 地区块儿颜色
		            itemStyle: {
		                normal: {
		                    areaColor: '#eee',
		                    borderColor: '#000000'
		                },
		                emphasis: {
		                    areaColor: '#21AEF8'
		                }
		            }
		        },
		        series: []
		    };
		
		//初始化Echarts
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
		
		//在每个城市上加上柱状图
		function renderEachCity() {
			console.log("开始画柱子！");
			var option = {
		        xAxis: [],
		        yAxis: [],
		        grid: [],
		        series: []
		    };
			
			echarts.util.each(rawData, function(dataItem, idx) {
				console.log("开始对每个城市进行处理！");
				var geoCoord = geoCoordMap[dataItem[0]];
				var coord = myChart.convertToPixel('geo', geoCoord);
				//测试
				console.log("dataItem:" + dataItem[0]);
				console.log("geoCoord:" + geoCoord);
				console.log("coord：" + coord);
				
				//数组中的序号转为字符串
				idx += '';
				
				//存储商品销量
				var inflationData = [dataItem[1]];
				
				//对商品销量进行处理, 四舍五入
				var dealInflationData;
				if(dataItem[3] == 'city'){
					dealInflationData = Math.round(dataItem[1] / 80);
				}
				else if(dataItem[3] == 'town'){
					dealInflationData = Math.round(dataItem[1] / 80);
				}
				console.log("dealInflationData:" + dealInflationData);
				
				//对柱子位置进行处理
				var leftCoord, topCoord;
				if(dataItem[3] == 'city'){
					leftCoord = coord[0] - 25;
					topCoord = coord[1] - 25;
				}
				else if(dataItem[3] == 'town'){
					leftCoord = coord[0];
					topCoord = coord[1];
				}
				
				//存放商品种类名称
				var categoryName = dataItem[2];
				
				option.xAxis.push({
					id: idx,
		            gridId: idx,
		            type: 'category',
		            name: dataItem[0],
		            nameLocation: 'middle',
		            nameGap: 3,
		            splitLine: {
		                show: false
		            },
		            axisTick: {
		                show: false
		            },
		            axisLabel: {
		                show: false
		            },
		            axisLine: {
		                onZero: false,
		                lineStyle: {
		                    color: '#666'
		                }
		            },
		            // data: xAxisCategory,
		            data: [categoryName],
		            z: 100
				});
				
				option.yAxis.push({
		            id: idx,
		            gridId: idx,
		            splitLine: {
		                show: false
		            },
		            axisTick: {
		                show: false
		            },
		            axisLabel: {
		                show: false
		            },
		            axisLine: {
		                show: false,
		                lineStyle: {
		                    color: '#1C70B6'
		                }
		            },
		            z: 100
		        });
				
				//设置柱子的形状
				option.grid.push({
		            id: idx,
		            width: 30,
		            height: dealInflationData,
		            left: leftCoord ,
		            top: topCoord ,
		            z: 100
		        });
				
				option.series.push({
		            id: idx,
		            type: 'bar',
		            xAxisId: idx,
		            yAxisId: idx,
		            barGap: 0,
		            barCategoryGap: 0,
		            data: inflationData,
		            z: 100,
		            itemStyle: {
		                normal: {
		                    color: function(params){
								//console.log(params);
								console.log(params.name);
								//返回柱子的颜色
	                            return categoryColor[params.name];
		                    }
		                }
		            }
		        });
			});
			myChart.setOption(option);
		}
		
		//从后台获取数据填充到js数组中
		function readData(myEcharts) {
			$.ajax({
				// 输入请求的路径,已经在index控制器里了
				url: "selectDataForMaxCategory",
				// 请求的类型
				type: "get",
				contentType: "application/json; charset=utf-8",
				// 返回的数据类型
				dataType: "json",
				success: function (data) {
					//测试
					console.log("data:" + data);
					console.log("开始读取数据！");
		
					//分条件获取数据
					if(data[0].selectedCC == "city"){
						for(var i = 0; i < data.length; i++){
							var maxnum = data[i].maxnum;
							var city = data[i].city;
							var longititude = data[i].longititude;
							var lantitude = data[i].lantitude;
							var category_name = data[i].category_name;
							var selectedCC = data[i].selectedCC;
		
							//创建1个数组用来存放城市,商品类型的销量和商品类型
							var row = new Array();
							row.push(city);
							row.push(maxnum);
							row.push(category_name);
							row.push(selectedCC);
							rawData.push(row);
		
							//创建1个数组用来存放经纬度
							var llArray = new Array();
							llArray[0] = longititude;
							llArray[1] = lantitude;
							geoCoordMap[city] = llArray;
							
						}
					}
					else if(data[0].selectedCC == "town"){
						for(var i = 0; i < data.length; i++){
							var maxnum = data[i].maxnum;
							var town = data[i].town;
							var longititude = data[i].longititude;
							var lantitude = data[i].lantitude;
							var category_name = data[i].category_name;
							var selectedCC = data[i].selectedCC;
							
							//创建1个数组用来存放城市和商品类型的销量
							var row = [];
							row.push(town);
							row.push(maxnum);
							row.push(category_name);
							row.push(selectedCC);
							rawData.push(row);
		
							//创建1个数组用来存放经纬度
							var llArray = [];
							llArray[0] = longititude;
							llArray[1] = lantitude;
							geoCoordMap[town] = llArray;
							
						}
					}
					//测试
					console.log("读取完成！");
					console.log("geoCoordMap:");
					console.log(geoCoordMap);
					console.log("rawData:");
					console.log(rawData);
					
					renderEachCity();
					console.log("柱状图绘制完成！")
				}
				
			});
			
		}

		//调用函数
		readData(myChart);
		
		setTimeout(renderEachCity, 0);
	    // 缩放和拖拽
	    function throttle(fn, delay, debounce) {

	        var currCall;
	        var lastCall = 0;
	        var lastExec = 0;
	        var timer = null;
	        var diff;
	        var scope;
	        var args;

	        delay = delay || 0;

	        function exec() {
	            lastExec = (new Date()).getTime();
	            timer = null;
	            fn.apply(scope, args || []);
	        }

	        var cb = function() {
	            currCall = (new Date()).getTime();
	            scope = this;
	            args = arguments;
	            diff = currCall - (debounce ? lastCall : lastExec) - delay;

	            clearTimeout(timer);

	            if (debounce) {
	                timer = setTimeout(exec, delay);
	            } else {
	                if (diff >= 0) {
	                    exec();
	                } else {
	                    timer = setTimeout(exec, -diff);
	                }
	            }

	            lastCall = currCall;
	        };

	        return cb;
	    }

	    var throttledRenderEachCity = throttle(renderEachCity, 0);
	    myChart.on('geoRoam', throttledRenderEachCity);
	    
	</script>

</body>
</html>