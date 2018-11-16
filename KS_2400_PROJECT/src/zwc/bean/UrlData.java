package zwc.bean;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import org.jsoup.nodes.Document;


public class UrlData {
	
	private static final String REG_URL = "((https|http|ftp|rtsp|mms){0,1}"
			+ "(:\\/\\/){0,1})(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+";
	
	private List<String> urlList;
	
	private UrlData() {
		urlList = new ArrayList<>();
	}

	public static UrlData parseDoc(Document doc) {
		UrlData urlData = new UrlData();
		urlData.resolveDoc(doc, "div .clamp-5");
		return urlData;
	}
	
	private void resolveDoc(Document doc, String className) {
		for (org.jsoup.nodes.Element element : doc.select(className) ) {
			System.out.println(element);
			//matcheContent(element.text(), reg);
		}
	}
	
	
}
