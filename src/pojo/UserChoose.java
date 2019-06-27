package pojo;

/*
 * 这是用来收集用户的表单数据
 * */

public class UserChoose {
	private String month;
	private String cityOrCounty;
	private String dataCategory;
	
	//属性的get,set方法
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getCityOrCounty() {
		return cityOrCounty;
	}
	public void setCityOrCounty(String cityOrCounty) {
		this.cityOrCounty = cityOrCounty;
	}
	
	public String getDataCategory() {
		return dataCategory;
	}
	public void setDataCategory(String dataCategory) {
		this.dataCategory = dataCategory;
	}
	
}
