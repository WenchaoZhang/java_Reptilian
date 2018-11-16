package zdg.model;

public class CityBacker {
	private String _cityName;
	private String _countryName;
	private String _backerNum;
	public CityBacker(String cityName,String countryName,String backerNum) {
		_cityName = cityName;
		_countryName = countryName;
		_backerNum = backerNum;
	}
	public String getCountryName() {
		return _countryName;
	}
	public String getCityName() {
		return _cityName;
	}
	public String getBackerNum() {
		return _backerNum;
	}
}
