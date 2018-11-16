package pageUtil;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import zdg.model.WebData;

public class PageUtil {
	private Document document;
	
	private static final String REG_URL = "((https|http|ftp|rtsp|mms){0,1}" 
											+ "(:\\/\\/){0,1})(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+";
	
	public Document getDocument(String url){
		if(url.matches(REG_URL)){
			try {
				return  Jsoup.parse(new URL(url+"/community"), 30000);
			} catch (IOException e) {
				System.err.println(url+":connect timeout!");
				return null;
			}			
		}else{
			return null;
		}
	}
	
	
	
}
