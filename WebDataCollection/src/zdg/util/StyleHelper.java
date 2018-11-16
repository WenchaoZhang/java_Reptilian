package zdg.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class StyleHelper {
	private short DEFAULT_SIZE=12;
	private String DEFAULT_FONT="宋体";
	private HSSFWorkbook _wb;
	private HSSFCellStyle defaultStyle;
	private HSSFCellStyle defaultStyleWithWrapText;
	private HSSFCellStyle titStyle;
	public StyleHelper(HSSFWorkbook wb) {
		_wb = wb;
		defaultStyle = getDefaultStyleWithWrapText(false);
		defaultStyleWithWrapText = getDefaultStyleWithWrapText(true);
		titStyle = getStyle(true, HSSFColorPredefined.GREY_40_PERCENT.getIndex(),
				"微软雅黑", DEFAULT_SIZE, true);
	}
	/**
	 * TODO 获取默认样式
	 * @param flag 是否自动换行
	 * @return    
	 * @author 张定国
	 * 2018年11月12日
	 */
	public HSSFCellStyle getDefaultStyle(boolean flag){
		HSSFCellStyle cellStyle = _wb.createCellStyle();
		cellStyle.cloneStyleFrom(flag?defaultStyleWithWrapText:defaultStyle);
		return cellStyle;
	}
	/**
	 * TODO 获取默认背景样式
	 * @return    
	 * @author 张定国
	 * 2018年11月12日
	 */
	public HSSFCellStyle getDefaultTltleStyle(){
		HSSFCellStyle cellStyle = _wb.createCellStyle();
		cellStyle.cloneStyleFrom(titStyle);
		return cellStyle;
	}
	/**
	 * TODO  创建默认样式，设置是否自动换行
	 * @param wb 工作簿
	 * @param flag 是否自动换行
	 * @return    
	 * @author 张定国
	 * 2018年11月12日
	 */
	public HSSFCellStyle getDefaultStyleWithWrapText(boolean flag) {
		return getDefaultStyleWithWrapText(flag,HSSFColorPredefined.WHITE.getIndex());
	}
	/**
	 * TODO 创建默认样式，设置背景色
	 * @param flag
	 * @param color
	 * @return    
	 * @author 张定国
	 * 2018年11月12日
	 */
	public HSSFCellStyle getDefaultStyleWithWrapText(boolean flag,short color) {
		return getStyle(true, color, DEFAULT_FONT, DEFAULT_SIZE, false);
	}
	/**
	* @Title: getStyle  
	* @Description: TODO
	* @param @param flag 
	* @param @param color
	* @param @param fontName
	* @param @param fontSize
	* @param @param isBold
	* @param @return    
	* @return HSSFCellStyle    
	* @throws
	 */
	public HSSFCellStyle getStyle(boolean flag,short color,
			String fontName,short fontSize,boolean isBold) {
		HSSFCellStyle cellStyle = _wb.createCellStyle();
		HSSFFont hssfFont = _wb.createFont();
		hssfFont.setFontHeightInPoints(fontSize);
		hssfFont.setFontName(fontName);
		hssfFont.setBold(isBold);
		cellStyle.setFont(hssfFont);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setWrapText(flag);
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		setBorder(cellStyle);
		return cellStyle;
	}
	/**
	* @Title: setBorder 边框设置
	* @Description: TODO
	* @param @param cellStyle    
	* @return void    
	* @throws
	 */
	private void setBorder(HSSFCellStyle cellStyle) {
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setRightBorderColor(HSSFColorPredefined.BLACK.getIndex());
		cellStyle.setLeftBorderColor(HSSFColorPredefined.BLACK.getIndex());
		cellStyle.setTopBorderColor(HSSFColorPredefined.BLACK.getIndex());
		cellStyle.setBottomBorderColor(HSSFColorPredefined.BLACK.getIndex());
		
	}
}
