package service;

import java.util.List;
import java.util.Map;

import pojo.sale;
import pojo.yearmonthInSale;

public interface saleService {
	public List<sale> selectShopSum(Map<String, Object> map, String selectedCC);
	public List<sale> selectMaxCategory(Map<String, Object> map, String selectedCC);
	public List<yearmonthInSale> selectMonth();
}
