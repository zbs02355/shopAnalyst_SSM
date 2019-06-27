package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import pojo.UserChoose;
import pojo.sale;
import pojo.yearmonthInSale;
import service.saleService;

@Controller("indexController")
@RequestMapping(value="/index")
public class indexController {
	//创建用于保存用户选择的月份、城市级别、展示数据类型的变量
	private String selectedMonth = "", selectedCityLevel = "";
	private String selectedDataCategory = "";
	
	//创建1个数组用于保存查询出的月份
	private String[] monthArray = null;
	
	@Autowired
	private saleService saleService;
	
	//处理首页
	@RequestMapping("/comein")
	public String comeinAndInit(Model model) {
		model.addAttribute("userChoose", new UserChoose());
		//从数据库中查询出月份并放到前端的月份列表中
		int index = 0; //数组下标
		List<yearmonthInSale> monthInDB = saleService.selectMonth();
		String[] month = new String[monthInDB.size()];
		for (yearmonthInSale aMonth : monthInDB) {
			month[index] = aMonth.getYearmonth();
			index++;
		}
		model.addAttribute("months", month);
//		//向市/县列表添加值
//		model.addAttribute("cityOrCountys", new String[] {"市","县"});
//		//向展示数据列表添加值
//		model.addAttribute("dataCategories", new String[] {"查看不同市县购买力活跃程度", "查看不同市县销量最高的商品类型"});
		
		//将月份数据保存到类中
		this.monthArray = new String[monthInDB.size()];
		this.monthArray = month;
		
		return "map";
	}
	
	//处理用户选择的数据
	@RequestMapping("userChoose")
	public String saveUserChoose(@ModelAttribute UserChoose userChoose, Model model) {
		//为表单创建一些属性
		model.addAttribute("months", this.monthArray);
		
		//创建2个Map进行英汉转换
		Map<String, String> cityOrCounty = new HashMap<String, String>();
		cityOrCounty.put("市", "city");
		cityOrCounty.put("县", "town");
		Map<String, String> dataCategory = new HashMap<String, String>();
		dataCategory.put("查看不同市县购买力活跃程度", "shopSum");
		dataCategory.put("查看不同市县销量最高的商品类型", "maxCategory");
		
		//获取用户选择的月份
		String selectedMonth = userChoose.getMonth();
		//获取用户选择的城市级别
		String selectedCityLevel = cityOrCounty.get(userChoose.getCityOrCounty());
		//获取用户选择的数据类型
		String selectedDataCategory = dataCategory.get(userChoose.getDataCategory());
		
		//保存月份、城市级别、数据类型到类的变量中
		this.selectedMonth = selectedMonth;
		this.selectedCityLevel = selectedCityLevel;
		this.selectedDataCategory = selectedDataCategory;
		
		//转发到第2个Map界面
		if(this.selectedDataCategory.equals("maxCategory")) {
			return "forward:/index/initSecondMap";
		}
		
		//测试
		System.out.println("\nselectedMonth:" + this.selectedMonth);
		System.out.println("selectedCityLevel:" + this.selectedCityLevel);
		System.out.println("selectedDataCategory:" + this.selectedDataCategory);;
		
		userChoose = null;
		
		//创建Userchoose接受下一次表单提交的数据
		model.addAttribute("userChoose", new UserChoose());
		
		//返回地图页面
		return "map";
	}
	
	//处理用户在第二个页面选择的数据
	@RequestMapping(value="userChooseInSecond")
	public String saveUserChooseInSecond(@ModelAttribute UserChoose userChoose, Model model) {
		//为表单创建一些属性
		model.addAttribute("months", this.monthArray);
				
		//创建2个Map进行英汉转换
		Map<String, String> cityOrCounty = new HashMap<String, String>();
		cityOrCounty.put("市", "city");
		cityOrCounty.put("县", "town");
				
		//获取用户选择的月份
		String selectedMonth = userChoose.getMonth();
		//获取用户选择的城市级别
		String selectedCityLevel = cityOrCounty.get(userChoose.getCityOrCounty());
		
		//保存月份、城市级别、数据类型到类的变量中
		this.selectedMonth = selectedMonth;
		this.selectedCityLevel = selectedCityLevel;
		
		//测试
		System.out.println("\nselectedMonth:" + this.selectedMonth);
		System.out.println("selectedCityLevel:" + this.selectedCityLevel);
		
		userChoose = null;
		
		//创建Userchoose接受下一次表单提交的数据
		model.addAttribute("userChoose", new UserChoose());
		
		//返回地图页面
		return "map2";
	}
	
	//根据用户选择为购买力活跃程度查询数据
	@RequestMapping(value = "selectDataForShopSum")
	@ResponseBody
	public List<sale> selectMapData(Model model) {
		//创建一个list用于表示最终查询的结果
		List<sale> resultList = new ArrayList<sale>();
		
		if(this.selectedDataCategory.equals("shopSum")) {
			//测试
			System.out.println("Ajax请求执行...");
			
			//创建两个变量用来存放默认数据和城市级别
			String defaultMonth = "201407";
			String defaultCC = "city";
			if ((this.selectedMonth.length() != 0) && (this.selectedCityLevel.length()) != 0) {
				defaultMonth = this.selectedMonth;
				defaultCC = this.selectedCityLevel;
			}
			//创建1个Map存放月份和城市级别
			Map<String, Object> monthAndCityInSQL = new HashMap<String, Object>();
			monthAndCityInSQL.put("month", defaultMonth);
			monthAndCityInSQL.put("cityOrTown", defaultCC);

			//进行查询
			resultList = saleService.selectShopSum(monthAndCityInSQL, this.selectedCityLevel);
			System.out.println("\n查询成功！" + "\nController返回的数据：");
			for (sale sale : resultList) {
				System.out.print("\n\t数量：" + sale.getNum() + "  城市:" + sale.getCity() + "  县城：" + sale.getTown());
				System.out.print("  经度：" + sale.getLongititude() + "  纬度" + sale.getLantitude() + "\n");
			}

		}
		return resultList;
	}
	
	//初始化第二个页面
	@RequestMapping(value="/initSecondMap")
	public String initSecondMapPage(Model model) {
		//向Model中添加存放用户选择的属性
		model.addAttribute("userChoose", new UserChoose());
		
		//为表单创建一些属性
		model.addAttribute("months", this.monthArray);
		
		return "map2";
	}
	
	//根据用户选择为商品类型查询数据
	@RequestMapping(value="/selectDataForMaxCategory")
	@ResponseBody
	public List<sale> selectMapDataForSecondPage(Model model){
		//测试
		System.out.println("Ajax请求执行...");
				
		//创建1个Map存放月份和城市级别
		Map<String, Object> monthAndCityInSQL = new HashMap<String, Object>();
		monthAndCityInSQL.put("month", this.selectedMonth);
		monthAndCityInSQL.put("cityOrTown", this.selectedCityLevel);
				
		//创建一个list用于表示最终查询的结果
		List<sale> resultList = new ArrayList<sale>();
		
		//进行查询
		List<sale> maxCategory = saleService.selectMaxCategory(monthAndCityInSQL, this.selectedCityLevel);
		System.out.println("\n查询成功！\nController返回的数据：");
		for (sale sale : maxCategory) {
			System.out.print("\n\t商品数量：" + sale.getMaxnum() + "  城市:" + sale.getCity());
			System.out.print("商品种类：" + sale.getCategory_name() + "  经度：" + sale.getLongititude() + "  纬度" + sale.getLantitude());
		}
		resultList = maxCategory;
		
		//返回结果
		return resultList;
	}
}
