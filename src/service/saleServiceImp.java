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
		//��ȡ�������뵽sale�����У�����loc�ֶν��д���
		List<sale> shopNumInSale = saledaoObject.selectShopSum(map1);
		System.out.println("\nService������ݣ� ");
		for (sale aLine : shopNumInSale) {
			String locInLine = aLine.getLoc();
			List<String> longAndLanList = deviseLoc(locInLine);
			
			//���𿪵ľ�γ��д�뵽sale������
			aLine.setLongititude(longAndLanList.get(0));
			aLine.setLantitude(longAndLanList.get(1));
			
			//����ʱ��ѯ�ĳ��м���д�뵽sale������
			aLine.setSelectedCC(selectedCC);
			
			System.out.print("\n\t������" + aLine.getNum() + " ���У�" + aLine.getCity() + "  �سǣ�" + aLine.getTown());
			System.out.print("����:" + aLine.getLongititude() + "γ�ȣ�" + aLine.getLantitude() + "\n");
		}
		return shopNumInSale;
	}
	@Override
	public List<sale> selectMaxCategory(Map<String, Object> map1, String selectedCC) {
		//��ȡ�������뵽sale�����У�����loc�ֶν��д���
		List<sale> maxCategoryInSale = saledaoObject.selectMaxCategory(map1);
		for (sale aLine : maxCategoryInSale) {
			//��ȡһ���е�loc�ֶ�
			String locInSale = aLine.getLoc();
			List<String> longAndLanList = deviseLoc(locInSale);
			
			//���𿪵ľ�γ��д�뵽sale������
			aLine.setLongititude(longAndLanList.get(0));
			aLine.setLantitude(longAndLanList.get(1));
			
			//����ʱ��ѯ�ĳ��м���д�뵽sale������
			aLine.setSelectedCC(selectedCC);
		}
		return maxCategoryInSale;
	}
	@Override
	public List<yearmonthInSale> selectMonth() {
		return saledaoObject.selectMonth();
	}
	
	/*
	 * �������ã����loc�ֶ�
	 * */
	public List<String> deviseLoc(String locTemp){
		//�����ַ����ָ����
		StringTokenizer tokenizer = new StringTokenizer(locTemp, ",");
		//������ž�γ�ȵ��б�
		List<String> longAndLanInList = new ArrayList<String>();
		
		//lastWord:loc�еĵ�һ����afterWord:loc�еĵڶ���
		String lastWord = "", afterWord = "";
		//������γ�ȱ���
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
		
		//����γ��д�뵽�б��в�����
		longAndLanInList.add(longititude);
		longAndLanInList.add(lantitude);
		return longAndLanInList;
	}
}
