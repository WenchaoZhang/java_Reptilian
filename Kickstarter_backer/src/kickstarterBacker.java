import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java_excel_output.ExcelUtil;

public class kickstarterBacker {
	
	private static final String REG_URL = "((https|http|ftp|rtsp|mms){0,1}"
			+ "(:\\/\\/){0,1})(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+";
	
	private String _inPath;
	
	static List<Map<String, String>> result = new ArrayList<>();
	static List<Map<String, String>> resultcc = new ArrayList<>();
	
	static Map<String, Long> resultCity = new LinkedHashMap<>();
	static Map<String, Long> resultCountry = new LinkedHashMap<>();
	
	static String fileName = "KICKSTARTER_BACKER_AS.xls";        // 定义文件名
	static String fileNamecity = "KICKSTARTER_BACKER_AS_city.xls";        // 定义文件名
	static String headString = "KICKSTARTER_BACKER_AS";          // 定义表格标题
	static String sheetName = "KICKSTARTER_BACKER_AS";                  // 定义工作表表名
	static String filePath = "  ";             // 文件本地保存路径
	static String[] thead = {"项目名称(PROJECT_NAME)","城市名称(CITY_NAME)","人数(CITY_NUMBER)","国家名称(COUNTRIS_NAME)",
			"人数(COUNTRIS_NUMBER)","新的支持者(NEW_BACKER)","老的支持者(OLD_BACKER)","总人数(ALL_BACKER)","总金额(ALL_MONEY)" };                    // 定义表头内容
	static int[] sheetWidth = {10000,9000,5000,5000,5000,5000,5000,5000,5000 };   // 定义每一列宽度
	
	
	static String[] theadCityCountryNumber = {"城市名称(CITY_NAME)","人数(CITY_NUMBER)", "  ", "国家名称(COUNTRIS_NAME)", "人数(COUNTRIS_NUMBER)"};
	
	static int[] sheetWidthCityCountryNumber = {10000,9000,10000,10000,9000};   // 定义每一列宽度

	
	
	static int conuter_url=0;
	
	public kickstarterBacker(String inPath) {
		_inPath = pathTransfer(inPath);
	}
	
	public static void main(String[] args) throws IOException{
		
		if(args.length!=2) {
			System.out.println("please in put urlPath and emailPath....");
			System.exit(0);
		}
		
		kickstarterBacker col = new kickstarterBacker(args[0]);    //路径转换
		filePath = pathTransfer(args[1]);
		System.out.println("urlPath:" + col._inPath);
		System.out.println("outPath:" + filePath + "/" + fileName);
		
		System.out.println("start collecting....");
		col.resolve();                                       //数据处理
		System.out.println("collecting over:");
		
		writeContent();                                    //数据导出Excel
	}
	
	/**
	 * @Title resolve
	 * @Description 读取文件url列表，取出doc提取出我们的要素，放到result（Excel数据）
	 * @return
	 * @throws IOException    
	 * @author 张定国，张文超修改
	 * @exception 文件有点多，不想花时间整合，将就着看
	 * 2018年11月5日
	 */
	public void resolve() throws IOException{
		List<String> urlList = readUrl();
		
		for (String url : urlList) {
			List<String> contentCity = new ArrayList<>();
			List<String> contentCountries = new ArrayList<>();
			
			Document doc = GetDoc(url + "/community");     //由URL获取网页源码
			
			if(doc == null) {			
				System.out.println("正在处理第   " + conuter_url++ +"  条网址.......");
				continue;
			}
			
			//从网页源码中过滤数据  start
			resolveClass(doc, "div .js-locations-countries .location-list__item", contentCountries);   //国家
			String[] scontentCountries = contentCountries.toArray(new String[contentCity.size()]);			
			resolveClass(doc, "div .js-locations-cities .location-list__item", contentCity);    //城市
			String[] scontentCity = contentCity.toArray(new String[contentCity.size()]);
			String newBack = resolveBackerClass(doc, "div .new-backers .count");			            //newbacker			
			String oldBacker = resolveBackerClass(doc, "div .existing-backers .count");               //oldbacker
			String projectName = resolveBackerClass(doc, "div .NS_project_profile__title");               //项目名称			
			String allNumber = resolveBackerClass(doc, "div .NS_campaigns__spotlight_stats");        
			//从网页源码中过滤数据 end

			//把过滤过的数据放到Excel存储链表中 start
			for(int i = 0; i < scontentCity.length/2; i++){
				Map<String, String> map = new LinkedHashMap<>();
				
				if(i == 0 && projectName.split(":").length > 0){
					map.put(thead[0], projectName);
				}else{
					map.put(thead[0], "");
				}
				
				//数据统计开始
				if(scontentCity.length > i*2+1 && scontentCountries.length > i*2+1){
					if(resultCity.containsKey(scontentCity[i*2])){
						long tmp = Long.valueOf( scontentCity[i*2+1].replace(",", "") ) + resultCity.get(scontentCity[i*2]);
						resultCity.put(scontentCity[i*2], tmp);
//						System.out.println(scontentCity[i*2] + "   =   " + tmp);
					}else{
						resultCity.put(scontentCity[i*2], Long.valueOf( scontentCity[i*2+1].replace(",", "") ));
					}
					
					if(resultCountry.containsKey(scontentCountries[i*2])){
						long tmp = Long.valueOf( scontentCountries[i*2+1].replace(",", "") ) + resultCountry.get(scontentCountries[i*2]);
						resultCountry.put(scontentCountries[i*2], tmp);
						System.out.println(scontentCountries[i*2] + "   =   " + tmp);
					}else{
						resultCountry.put(scontentCountries[i*2], Long.valueOf( scontentCountries[i*2+1].replace(",", "") ));
					}
					//数据统计结束
					map.put(thead[1], scontentCity[i*2]);
					map.put(thead[2], scontentCity[i*2+1]);
					map.put(thead[3], scontentCountries[i*2]);
					map.put(thead[4], scontentCountries[i*2+1]);
				}else{
					map.put(thead[1], "");
					map.put(thead[2], "");
					map.put(thead[3], "");
					map.put(thead[4], "");
				}
				
				if(i == 0){
					map.put(thead[5], newBack);
					map.put(thead[6], oldBacker);
					
					if(allNumber.split(" ").length > 0)
						map.put(thead[7], allNumber.split(" ")[0]);
					else
						map.put(thead[7], "");
						
					if(allNumber.split(" ").length >= 3)
					 map.put(thead[8], allNumber.split(" ")[3]);
					else
					 map.put(thead[8], "");
				}else{
					map.put(thead[5], "");
					map.put(thead[6], "");
					map.put(thead[7], "");
					map.put(thead[8], "");
				}
				result.add(map);
			}
			//把过滤过的数据放到Excel存储链表中 end

			
			//添加空行 start
			Map<String, String> map = new LinkedHashMap<>();
			for(int i = 0; i < 9; i++){
				map.put(thead[i], "");
			}
			result.add(map);
			//添加空行 end
			
			System.out.println("正在处理第   " + conuter_url++ +"  条网址.......");
		}
	}
	
	/**
	 * @Title pathTransfer
	 * @Description 路径处理(d:\xx\xx.txt-->d:/xx/xx.txt)
	 * @param path
	 * @return    
	 * @author 张定国
	 * 2018年11月5日
	 */
	private static String pathTransfer(String path) {
		return path.replace("\\", "/");
	}
	
	/**
	 * @Title readUrl
	 * @Description 读取文件url列表
	 * @return
	 * @throws IOException    
	 * @author 张定国
	 * 2018年11月5日
	 */
	private List<String> readUrl() throws IOException{
		List<String> urlList = new ArrayList<>();
		FileReader fr = new FileReader(new File(_inPath));
		BufferedReader reader = new BufferedReader(fr);
		String url;
		while((url=reader.readLine())!=null) {
			if(url.matches(REG_URL)&&!urlList.contains(url)) {
				urlList.add(url);
			}
		}
		reader.close();
		fr.close();
		return urlList;
	}
	
	/**
	 * @Title GetDoc
	 * @Description 解析url,获取页面指定正则表示的内容
	 * @param url
	 * @param reg    
	 * @author 张文超
	 * 2018年11月12日
	 */
	private Document GetDoc(String url){
		if(url.matches(REG_URL)) {
			try {
				//20秒超时连接
//				Document doc = Jsoup.parse(new URL(url), 20000);
				return Jsoup.parse(new URL(url), 20000);
			} catch (IOException e) {
				System.err.println(url+":connect timeout!");
			}
		}
		return null;
	}
	
	/**
	 * @Title resolveClass
	 * @Description 解析标签，内容
	 * @param doc 页面
	 * @param tagName 标签 
	 * @param reg 标签内容要匹配的规则
	 * @author 张文超
	 */
	private void resolveClass(Document doc,String className, List<String> content){
		for (Element element : doc.select(className) ) {
			content.add(element.select(".left").text());
			content.add(element.select(".right").text().split(" ")[0]);
		}
	}
	
	private String resolveBackerClass(Document doc,String className){
		return doc.select(className).text();
	}
	
	/**
	 * @Title writeContent
	 * @Description 数据导出到Excel
	 * @author 张文超
	 */
	private static void writeContent() throws IOException{
		HSSFWorkbook wb = new HSSFWorkbook();           // 创建Excel文档对象
		HSSFSheet sheet = wb.createSheet(sheetName);    // 创建工作表
		
		ExcelUtil.createHeadTittle(wb, sheet, headString, result.get(0).size() - 1);
		ExcelUtil.createThead(wb, sheet, thead, sheetWidth);
		ExcelUtil.createTable(wb, sheet, result);

		FileOutputStream fos = new FileOutputStream(new File(filePath+fileName));
		// filePath,fileName是如上定义的文件保存路径及文件名
		wb.write(fos);

		fos.close();
		wb.close();
		
		for(int i=0; i<resultCity.size(); i++){
			
		}
		
		for(Map.Entry entry:resultCity.entrySet()){
//			if(value.equals(entry.getValue())return entry.getKey();
			Map<String, String> map = new LinkedHashMap<>();
			map.put(theadCityCountryNumber[0], entry.getKey().toString());
			map.put(theadCityCountryNumber[1], entry.getValue().toString());			
			map.put(theadCityCountryNumber[2], "");
			map.put(theadCityCountryNumber[3], "");			
			map.put(theadCityCountryNumber[4], "");
			resultcc.add(map);
		}
		
		for(Map.Entry entry:resultCountry.entrySet()){
//			if(value.equals(entry.getValue())return entry.getKey();
			Map<String, String> map = new LinkedHashMap<>();
			map.put(theadCityCountryNumber[0], "");
			map.put(theadCityCountryNumber[1], "");			
			map.put(theadCityCountryNumber[2], "");
			map.put(theadCityCountryNumber[3], entry.getKey().toString());			
			map.put(theadCityCountryNumber[4], entry.getValue().toString());
			resultcc.add(map);
		}
//		
		
		
		HSSFWorkbook cc = new HSSFWorkbook();           // 创建Excel文档对象
		HSSFSheet sheetcc = cc.createSheet("CityCountry.xls");    // 创建工作表
		
		ExcelUtil.createHeadTittle(cc, sheetcc, "CityCountry"+headString, resultcc.get(0).size() - 1);
		ExcelUtil.createThead(cc, sheetcc, theadCityCountryNumber, sheetWidthCityCountryNumber);
		ExcelUtil.createTable(cc, sheetcc, resultcc);

		FileOutputStream foscc = new FileOutputStream(new File(filePath+fileNamecity));
		// filePath,fileName是如上定义的文件保存路径及文件名
		cc.write(foscc);

		foscc.close();
		cc.close();
	}
}
