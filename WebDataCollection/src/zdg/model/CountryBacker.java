package zdg.model;

public class CountryBacker {
	private String _countryName;
	private String _backerNum;
	public CountryBacker(String countryName,String backerNum) {
		_countryName = countryName;
		_backerNum = backerNum;
	}
	public String getCountryName() {
		return _countryName;
	}
	public String getBackerNum() {
		return _backerNum;
	}
}
