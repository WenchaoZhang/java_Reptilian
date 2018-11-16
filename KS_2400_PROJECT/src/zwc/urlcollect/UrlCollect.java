package zwc.urlcollect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import zwc.bean.UrlData;


public class UrlCollect {
	
	private static final String REG_URL = "((https|http|ftp|rtsp|mms){0,1}"
			+ "(:\\/\\/){0,1})(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+";
	
	private List<UrlData> content;
	private String _inPath;

	public UrlCollect(String inPath) {
		content = new ArrayList<>();
		_inPath = pathTransfer(inPath);
		System.out.println(_inPath);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("starting.....");
//		new UrlCollect("F:\\urlList.txt").resolve();
		List<String> urlList = new ArrayList<>();
		
		for(int pages=0; pages<201; pages++){
			try {
				//20秒超时连接
				Document doc = Jsoup.parse(new URL("https://www.kickstarter.com/discover/advanced?category_id=9&sort=most_funded&seed=2570062&page=" + pages), 50000);
	//			System.out.println(doc);

//				
				UrlData data = UrlData.parseDoc(doc);
				
				for (org.jsoup.nodes.Element element : doc.getElementsByClass("js-react-proj-card") ) {
					String[] tmps = element.toString().split("&quot;,&quot;blurb&quot");
					

					String[] tmps2 = tmps[1].split("&quot;");
					String   tmps3 = tmps2[tmps2.length - 1].toLowerCase()
										.replace("$", "")
										.replace(": ", "-")
										.replace("'", "")
										.replace("–", "")
										.replace("---", "-")
										.replace("{", "-")
										.replace("}", "-")
										.replace("(", "-")
										.replace(")", "-")
										.replace("--", "-")
										.replace(" - ", "-")
										.replace("- ", "-")
										.replace(" -", "-")
										.replace(" | ", "-")
										.replace(", ", "-")
										.replace(". ", "-")
										.replace("!", "")
										.replace(".", "")
										.replace("/", "-")
										.replace("\\", "-")
										.replace(" - ", "-")
										.replace("- ", "-")
										.replace(" ", "-")
										.replace(" ", "-")
										.replace("+", "")
										.replace("---", "-")
										.replace("--", "-")
										.replace(" - ", "-")
										.replace("- ", "-")
										.replace("---", "-")
										.replace("--", "-")
										.replace(" - ", "-")
										.replace("- ", "-");
					
					if(tmps3.length() < 100 && tmps3.indexOf("&") < 0){
						String tmps4;
						if(tmps3.length() > 50) {
							tmps4 = tmps3.substring(0,50);
						}else{
							tmps4 = tmps3;
						}
						System.out.println(tmps4);
						urlList.add("https://www.kickstarter.com/projects/161302347/" + tmps4);
					}
					
				}
			} catch (IOException e) {
				System.err.println(":connect timeout!");
			}
		}

		FileWriter fw = new FileWriter(new File("F:/doc.txt"));
		BufferedWriter bw = new BufferedWriter(fw);
		
		for (String str : urlList) {
			bw.write(str);
			bw.newLine();
		}

		bw.close();
		fw.close();
		
		System.out.println("end");
	}
	
	
	public void resolve() throws IOException {
		List<String> urlList = readUrl();
		System.out.println(urlList);
		for (String url : urlList) {
			UrlData data = resolvUrl(url);
		}
	}

	private String pathTransfer(String path) {
		return path.replace("\\", "/");
	}
	
	/**
	 * @Title readUrl
	 * @Description 读取文件url列表
	 * @return
	 * @throws IOException    
	 * @author 张定国
	 */
	private List<String> readUrl() throws IOException{
		List<String> urlList = new ArrayList<>();
		FileReader fr = new FileReader(new File(_inPath));
		BufferedReader reader = new BufferedReader(fr);
		String url;
		while((url=reader.readLine())!=null) {
			System.out.println(url);
			if(!urlList.contains(url)) {
				urlList.add(url);
			}
		}
		reader.close();
		fr.close();
		return urlList;
	}
	
	
	/**
	 * @Title ResolvUrl
	 * @Description 解析url,获取页面指定正则表示的内容
	 * @param url
	 * @param reg    
	 * @author 张定国
	 */
	private UrlData resolvUrl(String url){
		//if(url.matches(REG_URL)) {
			try {
				//20秒超时连接
				Document doc = Jsoup.parse(new URL(url), 20000);
				System.out.println(doc);
				UrlData data = UrlData.parseDoc(doc);
				return data;
			} catch (IOException e) {
				System.err.println(url+":connect timeout!");
			}
		//}
		return null;
	}
	
}
