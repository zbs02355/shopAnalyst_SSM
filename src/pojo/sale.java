package pojo;

/*
 * 作用：用于查询销售总数和最高种类
 * */

public class sale {
	private String yearmonth;
	private String hub_type; 
	private String store_seq;
	private String local_channel_type;
	private String local_store_type;
	private char golden_store_flag;
	private String province;
	private String city;
	private String town;
	private char city_level; 
	private String item_code;
	private String product_chinese_name;
	private String category_name;
	private String brand_name;
	private String tier_name;
	private String variant_name;
	private String loc;
	private Integer num;
	private Integer maxnum;
	private String longititude;
	private String lantitude; 
	private String selectedCC;
	
	//一些属性的get,set方法
	public String getSelectedCC() {
		return selectedCC;
	}
	public void setSelectedCC(String selectedCC) {
		this.selectedCC = selectedCC;
	}
	
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	//销售总数
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	//商品种类总数
	public Integer getMaxnum() {
		return maxnum;
	}
	public void setMaxnum(Integer maxnum) {
		this.maxnum = maxnum;
	}
	
	public String getLongititude() {
		return longititude;
	}
	public void setLongititude(String longititude) {
		this.longititude = longititude;
	}
	
	public String getLantitude() {
		return lantitude;
	}
	public void setLantitude(String lantitude) {
		this.lantitude = lantitude;
	}
	
}
