package zdg.model;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebData {
	private String _projectName;
	private List<CityBacker> _cities;
	private List<CountryBacker> _countries;
	private String _sumPrice;
	private String _allBackerNum;
	private String _newBackerNum;
	private String _oldBackerNum;
	private WebData(Document doc) {
		_cities = new ArrayList<>();
		_countries = new ArrayList<>();
	}
	public static WebData parseDoc(Document doc) {
		WebData webData = new WebData(doc);
		webData.resolveDoc(doc);
		return webData;
	}
	
	/**
	* @projectName: resolveDoc  
	* @Description: 页面解析
	* @param @param doc    
	* @return void    
	* @throws
	 */
	private void resolveDoc(Document doc) {
		resolveProjectName(doc);
		resolveSummary(doc);
		resolveBackerNum(doc);
		resolveCityBackerNum(doc);
		resolveCountryBackerNum(doc);
	}
	/**
	* @Title: resolveProjectName  
	* @Description: 解析项目名
	* @param @param doc    
	* @return void    
	* @throws
	 */
	private void resolveProjectName(Document doc){
		 _projectName = doc.getElementsByClass("hero__link").text();
	}
	/**
	* @Title: resolveSummary  
	* @Description: 解析赞助汇总信息
	* @param @param doc    
	* @return void    
	* @throws
	 */
	private void resolveSummary(Document doc) {
		Elements allNum = doc.select(".NS_campaigns__spotlight_stats b");
		Elements money = doc.select(".NS_campaigns__spotlight_stats .money");
		_allBackerNum = allNum.text().split(" ")[0];
		_sumPrice = money.text();
	}
	/**
	 * @Title: resolveBackerNum  
	 * @Description: 解析赞助人数部分数据
	 * @param @param doc    
	 * @return void    
	 * @throws
	 */
	private void resolveBackerNum(Document doc) {
		Elements existNum = doc.select(".existing-backers .count");
		Elements newNum = doc.select(".new-backers .count");
		_oldBackerNum = existNum.text();
		_newBackerNum = newNum.text();
	}
	/**
	* @Title: resolveCityBackerNum  
	* @Description: 解析各城市赞助人数
	* @param @param doc    
	* @return void    
	* @throws
	 */
	private void resolveCityBackerNum(Document doc) {
		Elements citys = doc.select(".js-locations-cities .location-list__item");
		for (Element city : citys) {
			String cityName = city.select(".left .primary-text").text();
			String countryName = city.select(".left .secondary-text").text();
			String num = city.select(".right .tertiary-text").text().split(" ")[0];
			addCity(cityName, countryName,num);
		}
	}
	/**
	* @Title: resolveCountryBackerNum  
	* @Description: 解析各国家赞数人数
	* @param @param doc    
	* @return void    
	* @throws
	 */
	private void resolveCountryBackerNum(Document doc) {
		Elements citys = doc.select(".js-locations-countries .location-list__item");
		for (Element city : citys) {
			String countryName = city.select(".left .primary-text").text();
			String num = city.select(".right .tertiary-text").text().split(" ")[0];
			addCountry(countryName,num);
		}
	}
	public void addCity(String cityName,String countryName,String backerNum) {
		_cities.add(new CityBacker(cityName, countryName,backerNum));
	}
	public void addCountry(String countryName,String backerNum) {
		_countries.add(new CountryBacker(countryName, backerNum));
	}
	public String getProjectName() {
		return _projectName;
	}
	public String getNewBackerNum() {
		return _newBackerNum;
	}
	public String getOldBackerNum() {
		return _oldBackerNum;
	}
	public String getAllBackerNum() {
		return _allBackerNum;
	}
	public List<CityBacker> getCities() {
		return _cities;
	}
	public List<CountryBacker> getCountries() {
		return _countries;
	}
	public String getSumPrice() {
		return _sumPrice;
	}
}
