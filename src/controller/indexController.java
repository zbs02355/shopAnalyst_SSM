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
	//�������ڱ����û�ѡ����·ݡ����м���չʾ�������͵ı���
	private String selectedMonth = "", selectedCityLevel = "";
	private String selectedDataCategory = "";
	
	//����1���������ڱ����ѯ�����·�
	private String[] monthArray = null;
	
	@Autowired
	private saleService saleService;
	
	//������ҳ
	@RequestMapping("/comein")
	public String comeinAndInit(Model model) {
		model.addAttribute("userChoose", new UserChoose());
		//�����ݿ��в�ѯ���·ݲ��ŵ�ǰ�˵��·��б���
		int index = 0; //�����±�
		List<yearmonthInSale> monthInDB = saleService.selectMonth();
		String[] month = new String[monthInDB.size()];
		for (yearmonthInSale aMonth : monthInDB) {
			month[index] = aMonth.getYearmonth();
			index++;
		}
		model.addAttribute("months", month);
//		//����/���б����ֵ
//		model.addAttribute("cityOrCountys", new String[] {"��","��"});
//		//��չʾ�����б����ֵ
//		model.addAttribute("dataCategories", new String[] {"�鿴��ͬ���ع�������Ծ�̶�", "�鿴��ͬ����������ߵ���Ʒ����"});
		
		//���·����ݱ��浽����
		this.monthArray = new String[monthInDB.size()];
		this.monthArray = month;
		
		return "map";
	}
	
	//�����û�ѡ�������
	@RequestMapping("userChoose")
	public String saveUserChoose(@ModelAttribute UserChoose userChoose, Model model) {
		//Ϊ������һЩ����
		model.addAttribute("months", this.monthArray);
		
		//����2��Map����Ӣ��ת��
		Map<String, String> cityOrCounty = new HashMap<String, String>();
		cityOrCounty.put("��", "city");
		cityOrCounty.put("��", "town");
		Map<String, String> dataCategory = new HashMap<String, String>();
		dataCategory.put("�鿴��ͬ���ع�������Ծ�̶�", "shopSum");
		dataCategory.put("�鿴��ͬ����������ߵ���Ʒ����", "maxCategory");
		
		//��ȡ�û�ѡ����·�
		String selectedMonth = userChoose.getMonth();
		//��ȡ�û�ѡ��ĳ��м���
		String selectedCityLevel = cityOrCounty.get(userChoose.getCityOrCounty());
		//��ȡ�û�ѡ�����������
		String selectedDataCategory = dataCategory.get(userChoose.getDataCategory());
		
		//�����·ݡ����м����������͵���ı�����
		this.selectedMonth = selectedMonth;
		this.selectedCityLevel = selectedCityLevel;
		this.selectedDataCategory = selectedDataCategory;
		
		//ת������2��Map����
		if(this.selectedDataCategory.equals("maxCategory")) {
			return "forward:/index/initSecondMap";
		}
		
		//����
		System.out.println("\nselectedMonth:" + this.selectedMonth);
		System.out.println("selectedCityLevel:" + this.selectedCityLevel);
		System.out.println("selectedDataCategory:" + this.selectedDataCategory);;
		
		userChoose = null;
		
		//����Userchoose������һ�α��ύ������
		model.addAttribute("userChoose", new UserChoose());
		
		//���ص�ͼҳ��
		return "map";
	}
	
	//�����û��ڵڶ���ҳ��ѡ�������
	@RequestMapping(value="userChooseInSecond")
	public String saveUserChooseInSecond(@ModelAttribute UserChoose userChoose, Model model) {
		//Ϊ������һЩ����
		model.addAttribute("months", this.monthArray);
				
		//����2��Map����Ӣ��ת��
		Map<String, String> cityOrCounty = new HashMap<String, String>();
		cityOrCounty.put("��", "city");
		cityOrCounty.put("��", "town");
				
		//��ȡ�û�ѡ����·�
		String selectedMonth = userChoose.getMonth();
		//��ȡ�û�ѡ��ĳ��м���
		String selectedCityLevel = cityOrCounty.get(userChoose.getCityOrCounty());
		
		//�����·ݡ����м����������͵���ı�����
		this.selectedMonth = selectedMonth;
		this.selectedCityLevel = selectedCityLevel;
		
		//����
		System.out.println("\nselectedMonth:" + this.selectedMonth);
		System.out.println("selectedCityLevel:" + this.selectedCityLevel);
		
		userChoose = null;
		
		//����Userchoose������һ�α��ύ������
		model.addAttribute("userChoose", new UserChoose());
		
		//���ص�ͼҳ��
		return "map2";
	}
	
	//�����û�ѡ��Ϊ��������Ծ�̶Ȳ�ѯ����
	@RequestMapping(value = "selectDataForShopSum")
	@ResponseBody
	public List<sale> selectMapData(Model model) {
		//����һ��list���ڱ�ʾ���ղ�ѯ�Ľ��
		List<sale> resultList = new ArrayList<sale>();
		
		if(this.selectedDataCategory.equals("shopSum")) {
			//����
			System.out.println("Ajax����ִ��...");
			
			//�������������������Ĭ�����ݺͳ��м���
			String defaultMonth = "201407";
			String defaultCC = "city";
			if ((this.selectedMonth.length() != 0) && (this.selectedCityLevel.length()) != 0) {
				defaultMonth = this.selectedMonth;
				defaultCC = this.selectedCityLevel;
			}
			//����1��Map����·ݺͳ��м���
			Map<String, Object> monthAndCityInSQL = new HashMap<String, Object>();
			monthAndCityInSQL.put("month", defaultMonth);
			monthAndCityInSQL.put("cityOrTown", defaultCC);

			//���в�ѯ
			resultList = saleService.selectShopSum(monthAndCityInSQL, this.selectedCityLevel);
			System.out.println("\n��ѯ�ɹ���" + "\nController���ص����ݣ�");
			for (sale sale : resultList) {
				System.out.print("\n\t������" + sale.getNum() + "  ����:" + sale.getCity() + "  �سǣ�" + sale.getTown());
				System.out.print("  ���ȣ�" + sale.getLongititude() + "  γ��" + sale.getLantitude() + "\n");
			}

		}
		return resultList;
	}
	
	//��ʼ���ڶ���ҳ��
	@RequestMapping(value="/initSecondMap")
	public String initSecondMapPage(Model model) {
		//��Model����Ӵ���û�ѡ�������
		model.addAttribute("userChoose", new UserChoose());
		
		//Ϊ������һЩ����
		model.addAttribute("months", this.monthArray);
		
		return "map2";
	}
	
	//�����û�ѡ��Ϊ��Ʒ���Ͳ�ѯ����
	@RequestMapping(value="/selectDataForMaxCategory")
	@ResponseBody
	public List<sale> selectMapDataForSecondPage(Model model){
		//����
		System.out.println("Ajax����ִ��...");
				
		//����1��Map����·ݺͳ��м���
		Map<String, Object> monthAndCityInSQL = new HashMap<String, Object>();
		monthAndCityInSQL.put("month", this.selectedMonth);
		monthAndCityInSQL.put("cityOrTown", this.selectedCityLevel);
				
		//����һ��list���ڱ�ʾ���ղ�ѯ�Ľ��
		List<sale> resultList = new ArrayList<sale>();
		
		//���в�ѯ
		List<sale> maxCategory = saleService.selectMaxCategory(monthAndCityInSQL, this.selectedCityLevel);
		System.out.println("\n��ѯ�ɹ���\nController���ص����ݣ�");
		for (sale sale : maxCategory) {
			System.out.print("\n\t��Ʒ������" + sale.getMaxnum() + "  ����:" + sale.getCity());
			System.out.print("��Ʒ���ࣺ" + sale.getCategory_name() + "  ���ȣ�" + sale.getLongititude() + "  γ��" + sale.getLantitude());
		}
		resultList = maxCategory;
		
		//���ؽ��
		return resultList;
	}
}
