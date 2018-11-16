package zdg.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;

import zdg.util.StyleHelper;
public class ExcelModel {
	private HSSFWorkbook wb;
	private StyleHelper helper;
	private List<WebData> _webDataList;
	private int rowIndex = 1;
	HSSFSheet sheet;
	private String[] colTitle = {"项目名称","总人数","新的支持者","老的支持者","总金额","城市名称","人数","国家名称","人数"};
	int[] width= {6000,4000,4000,4000,4000,9000,4000,6000,4000};
	public ExcelModel(List<WebData> webDataList) {
		wb = new HSSFWorkbook();
		helper = new StyleHelper(wb);
		sheet = wb.createSheet("sheet1");
		_webDataList = webDataList;
	}
	public void createXls(String path) throws IOException {
		File file = new File(path);
		setWidth();
		HSSFRow row = sheet.createRow(0);
		setColTitle(row);
		setContent();
		wb.write(file);
		wb.close();
	}
	/**
	* @Title: setContent  
	* @Description: 写入内容
	* @param @param data    
	* @return void    
	* @throws
	 */
	private void setContent() {
		for (int i=0;i<_webDataList.size();i++) {
			WebData webData = _webDataList.get(i);
			HSSFRow row = sheet.createRow(rowIndex);
			setProjectName(row, 0, webData.getProjectName());
			setValueDefault(row, 1, webData.getAllBackerNum());
			setValueDefault(row, 2, webData.getNewBackerNum());
			setValueDefault(row, 3, webData.getOldBackerNum());
			setValueDefault(row, 4, webData.getSumPrice());
			if(webData.getCities().size()>0) {
				int tempIndex = rowIndex;
				setListValue(webData);
				mergeRegion(tempIndex, rowIndex,0,0);
				mergeRegion(tempIndex, rowIndex,1,1);
				mergeRegion(tempIndex, rowIndex,2,2);
				mergeRegion(tempIndex, rowIndex,3,3);
				mergeRegion(tempIndex, rowIndex,4,4);
			}
			rowIndex++;
		}
		
	}
	/**
	 * @param row 
	* @Title: setCity  
	* @Description: TODO 设置城市/国家内容
	* @param @param cities    
	* @return void    
	* @throws
	 */
	private void setListValue(WebData data) {
		HSSFRow row = sheet.getRow(rowIndex);
		List<CityBacker> cities = data.getCities();
		List<CountryBacker> countries = data.getCountries();
		setCityAndContry(row, cities.get(0), countries.get(0));
		for (int i=1;i<cities.size();i++) {
			rowIndex++;
			HSSFRow listRow = sheet.createRow(rowIndex);
			setCityAndContry(listRow, cities.get(i), countries.get(i));
		}
	}
	/**
	* @Title: setCityAndContry  
	* @Description: TODO 设置城市国家等内容
	* @param @param row
	* @param @param cityBacker
	* @param @param countryBacker    
	* @return void    
	* @throws
	 */
	private void setCityAndContry(HSSFRow row, CityBacker cityBacker,CountryBacker countryBacker) {
		setValueDefault(row, 5, cityBacker.getCityName()+" "+cityBacker.getCountryName());
		setValueDefault(row, 6, cityBacker.getBackerNum());
		setValueDefault(row, 7, countryBacker.getCountryName());
		setValueDefault(row, 8, countryBacker.getBackerNum());
		
	}
	/**
	 * TODO 列宽设置
	 * @param sheet
	 * @param width
	 * @param colNum    
	 * @author 张定国
	 * 2018年11月12日
	 */
	private void setWidth(){
		for(int i=0;i<width.length;i++) {
			sheet.setColumnWidth(i, width[i]);
		}
	}
	/**
	 * TODO 标题列设置
	 * @param row    
	 * @author 张定国
	 * 2018年11月12日
	 */
	private void setColTitle(HSSFRow row) {
		for(int i=0;i<colTitle.length;i++) {
			HSSFCellStyle cellStyle = helper.getDefaultTltleStyle();
			CellUtil.createCell(row, i, colTitle[i],cellStyle);
		}
	}
	/**
	* @Title: setProjectName  
	* @Description: TODO 设置项目名（可换行）
	* @param @param row
	* @param @param colNum
	* @param @param value    
	* @return void    
	* @throws
	 */
	private void setProjectName(HSSFRow row,int colNum,String value) {
		setCellValue(row, colNum, value, true);
	}
	/**
	* @Title: setCellValue  
	* @Description: TODO 设置单元格内容
	* @param @param row
	* @param @param colNum
	* @param @param value
	* @param @param flag    
	* @return void    
	* @throws
	 */
	private void setCellValue(HSSFRow row,int colNum,String value,boolean flag) {
		HSSFCellStyle cellStyle = helper.getDefaultStyle(flag);
		CellUtil.createCell(row, colNum, value,cellStyle);
	}
	/**
	* @Title: setValueDefault  
	* @Description: TODO 默认不换行
	* @param @param row
	* @param @param colNum
	* @param @param value    
	* @return void    
	* @throws
	 */
	private void setValueDefault(HSSFRow row,int colNum,String value) {
		setCellValue(row, colNum, value, true);
	}
	/**
	* @Title: mergeRegion 合并单元格
	* @Description: TODO
	* @param @param r1
	* @param @param r2
	* @param @param c1
	* @param @param c2    
	* @return void    
	* @throws
	 */
	private void mergeRegion(int r1,int r2,int c1,int c2) {
		CellRangeAddress region = new CellRangeAddress(r1, r2, c1, c2);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);    
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);  
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);    
		sheet.addMergedRegion(region);
	}
}
