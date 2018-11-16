package java_excel_output;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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

public class excelOutput {
	

	
	public excelOutput() {

	}
	
	public static void main(String[] args) throws IOException{
		
		List<Map<String, String>> result = new ArrayList<>();
		Map<String, String> map = new LinkedHashMap<>();
		
		String fileName = "KICKSTARTER_BACKER_AS.xls";        // 定义文件名
		String headString = "KICKSTARTER_BACKER_AS";          // 定义表格标题
		String sheetName = "KICKSTARTER_BACKER_AS";                  // 定义工作表表名
		String filePath = "F:\\";             // 文件本地保存路径
		String[] thead = {"项目名称(PROJECT_NAME)","城市名称(CITY_NAME)","人数(CITY_NUMBER)","国家名称(COUNTRIS_NAME)",
				"人数(COUNTRIS_NUMBER)","新的支持者(NEW_BACKER)","老的支持者(OLD_BACKER)","总人数(ALL_BACKER)","总金额(ALL_MONEY)" };                    // 定义表头内容
		int[] sheetWidth = {5000,5000,5000,5000,5000,5000,5000,5000,5000 };   // 定义每一列宽度

		map.put("thead[0]", "SNAPMAKER");
		map.put("CITY_NAME", "beijing");
		map.put("CITY_NUMBER", "12");
		map.put("COUNTRIS_NAME", "zhongguo");
		map.put("COUNTRIS_NUMBER", "210");
		map.put("NEW_BACKER", "NEW_BACKER");
		map.put("OLD_BACKER", "OLD_BACKER");		
		map.put("ALL_BACKER", "NEW_BACKER");
		map.put("ALL_MONEY", "OLD_BACKER");
		result.add(map);
		map.put("PROJECT_NAME", "SNAPMAKER");
		map.put("CITY_NAME", "beijing");
		map.put("CITY_NUMBER", "112");
		map.put("COUNTRIS_NAME", "zhongguo");
		map.put("COUNTRIS_NUMBER", "210");
		map.put("NEW_BACKER", "NEW_BACKER");
		map.put("OLD_BACKER", "OLD_BACKER");		
		map.put("ALL_BACKER", "NEW_BACKER");
		map.put("ALL_MONEY", "OLD_BACKER");
		result.add(map);

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

		System.out.println("start collecting....");
		System.out.println("collecting over:");
	}
}
