 package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import pojo.sale;
import pojo.yearmonthInSale;

@Repository("saleDao")
//使用mapper注解，替换掉sqlsession自带的方法
@Mapper
public interface saleDao {
	//方法名对应着映射文件元素的ID
	public List<sale> selectShopSum(Map<String, Object> map);
	public List<sale> selectMaxCategory(Map<String, Object> map);
	public List<yearmonthInSale> selectMonth();
}
