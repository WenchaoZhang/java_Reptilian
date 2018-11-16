package zdg.collect;

import java.io.BufferedReader;	
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import zdg.model.ExcelModel;
import zdg.model.WebData;

public class WebDataCollection {
	private static final String REG_URL = "((https|http|ftp|rtsp|mms){0,1}"
			+ "(:\\/\\/){0,1})(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+";
	
	private List<WebData> content;
	private String _inPath;
	private String _outPath;
	
	public WebDataCollection(String inPath,String outPath) {
		content = new ArrayList<>();
		_inPath = pathTransfer(inPath);
		_outPath = pathTransfer(outPath);
	}
	
	public static void main(String[] args) throws IOException {
		new WebDataCollection("F:\\urlList.txt","f:/test.xls").resolve();
	}
	
	public void resolve() throws IOException {
		List<String> urlList = readUrl();
		for (String url : urlList) {
			WebData data = resolvUrl(url);
			if(data!=null) {
				content.add(data);
			}
		}
		ExcelModel model = new ExcelModel(content);
		model.createXls(_outPath);
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
			if(url.matches(REG_URL)&&!urlList.contains(url)) {
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
	private WebData resolvUrl(String url){
		if(url.matches(REG_URL)) {
			try {
				//20秒超时连接
				Document doc = Jsoup.parse(new URL(url+"/community"), 20000);
				WebData data = WebData.parseDoc(doc);
				return data;
			} catch (IOException e) {
				System.err.println(url+":connect timeout!");
			}
		}
		return null;
	}
	
	/**
	 * @Title pathTransfer
	 * @Description 路径处理(d:\xx\xx.txt-->d:/xx/xx.txt)
	 * @param path
	 * @return    
	 * @author 张定国
	 */
	private String pathTransfer(String path) {
		return path.replace("\\", "/");
	}
}
