package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.saleDao;
import pojo.sale;
import pojo.yearmonthInSale;

@Service("saleServiceImp")
public class saleServiceImp implements saleService{
	@Autowired
	private saleDao saledaoObject;
	
	@Override
	public List<sale> selectShopSum(Map<String, Object> map1, String selectedCC) {
		//获取数据填入到sale对象中，并对loc字段进行处理
		List<sale> shopNumInSale = saledaoObject.selectShopSum(map1);
		System.out.println("\nService层的数据： ");
		for (sale aLine : shopNumInSale) {
			String locInLine = aLine.getLoc();
			List<String> longAndLanList = deviseLoc(locInLine);
			
			//将拆开的经纬度写入到sale对象中
			aLine.setLongititude(longAndLanList.get(0));
			aLine.setLantitude(longAndLanList.get(1));
			
			//将此时查询的城市级别写入到sale对象中
			aLine.setSelectedCC(selectedCC);
			
			System.out.print("\n\t数量：" + aLine.getNum() + " 城市：" + aLine.getCity() + "  县城：" + aLine.getTown());
			System.out.print("经度:" + aLine.getLongititude() + "纬度：" + aLine.getLantitude() + "\n");
		}
		return shopNumInSale;
	}
	@Override
	public List<sale> selectMaxCategory(Map<String, Object> map1, String selectedCC) {
		//获取数据填入到sale对象中，并对loc字段进行处理
		List<sale> maxCategoryInSale = saledaoObject.selectMaxCategory(map1);
		for (sale aLine : maxCategoryInSale) {
			//获取一行中的loc字段
			String locInSale = aLine.getLoc();
			List<String> longAndLanList = deviseLoc(locInSale);
			
			//将拆开的经纬度写入到sale对象中
			aLine.setLongititude(longAndLanList.get(0));
			aLine.setLantitude(longAndLanList.get(1));
			
			//将此时查询的城市级别写入到sale对象中
			aLine.setSelectedCC(selectedCC);
		}
		return maxCategoryInSale;
	}
	@Override
	public List<yearmonthInSale> selectMonth() {
		return saledaoObject.selectMonth();
	}
	
	/*
	 * 函数作用：拆分loc字段
	 * */
	public List<String> deviseLoc(String locTemp){
		//创建字符串分割对象
		StringTokenizer tokenizer = new StringTokenizer(locTemp, ",");
		//创建存放经纬度的列表
		List<String> longAndLanInList = new ArrayList<String>();
		
		//lastWord:loc中的第一个；afterWord:loc中的第二个
		String lastWord = "", afterWord = "";
		//创建经纬度变量
		String longititude = "", lantitude = "";
		
		while(tokenizer.hasMoreTokens()) {
			if(afterWord.equals("")) {
				lastWord = tokenizer.nextToken();
				afterWord = lastWord;
				continue;
			}
			afterWord = tokenizer.nextToken();
			if(Double.valueOf(lastWord) > Double.valueOf(afterWord)) {
				longititude = lastWord;
				lantitude = afterWord;
			}
			else if (Double.valueOf(lastWord) < Double.valueOf(afterWord)) {
				longititude = afterWord;
				lantitude = lastWord;
			}
		}
		
		//将经纬度写入到列表中并返回
		longAndLanInList.add(longititude);
		longAndLanInList.add(lantitude);
		return longAndLanInList;
	}
}
