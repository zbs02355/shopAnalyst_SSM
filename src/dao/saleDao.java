 package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import pojo.sale;
import pojo.yearmonthInSale;

@Repository("saleDao")
//ʹ��mapperע�⣬�滻��sqlsession�Դ��ķ���
@Mapper
public interface saleDao {
	//��������Ӧ��ӳ���ļ�Ԫ�ص�ID
	public List<sale> selectShopSum(Map<String, Object> map);
	public List<sale> selectMaxCategory(Map<String, Object> map);
	public List<yearmonthInSale> selectMonth();
}
