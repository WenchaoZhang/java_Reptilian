package pachong;

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
import org.jsoup.nodes.Element;

public class EmailCollection {
	private static final String REG_URL = "((https|http|ftp|rtsp|mms){0,1}"
			+ "(:\\/\\/){0,1})(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+";
	private static final String REG_EMAIL = "[A-Za-z\\d]+([-_.][A-Za-z\\d]+)"
			+ "*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}";
	private List<String> content;
	private String _inPath;
	private String _outPath;
	public EmailCollection(String inPath,String outPath) {
		content = new ArrayList<>();
		_inPath = pathTransfer(inPath);
		_outPath = pathTransfer(outPath);
	}
	public static void main(String[] args) throws IOException{
		if(args.length!=2) {
			System.out.println("please in put urlPath and emailPath....");
			System.exit(0);
		}
		EmailCollection col = new EmailCollection(args[0],args[1]);
		System.out.println("urlPath：" + col._inPath);
		System.out.println("emailPath：" + col._outPath);
		System.out.println("start collecting....");
		col.resolve();
		System.out.println("collecting over！");
	}
	/**
	 * @Title resolve
	 * @Description 读取文件内的url列表，爬取页面email
	 * @param inPath
	 * @param outPath
	 * @throws IOException    
	 * @author 张定国
	 * 2018年11月5日
	 */
	public void resolve() throws IOException{
		List<String> urlList = readUrl();
		for (String url : urlList) {
			resolvUrl(url,"a",REG_EMAIL);
			resolvUrl(url,"span",REG_EMAIL);
		}
		writeContent();
	}
	/**
	 * @Title pathTransfer
	 * @Description 路径处理(d:\xx\xx.txt-->d:/xx/xx.txt)
	 * @param path
	 * @return    
	 * @author 张定国
	 * 2018年11月5日
	 */
	private String pathTransfer(String path) {
		return path.replace("\\", "/");
	}
	/**
	 * @Title readUrl
	 * @Description 读取文件url列表
	 * @return
	 * @throws IOException    
	 * @author 张定国
	 * 2018年11月5日
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
	 * @Title writeContent
	 * @Description 输出网页读取内容到指定文件
	 * @throws IOException    
	 * @author 张定国
	 * 2018年11月5日
	 */
	private void writeContent() throws IOException {
		FileWriter fw = new FileWriter(new File(_outPath));
		BufferedWriter bw = new BufferedWriter(fw);
		for (String str : content) {
			bw.write(str);
			bw.newLine();
		}
		bw.close();
		fw.close();
	}
	/**
	 * @Title ResolvUrl
	 * @Description 解析url,获取页面指定正则表示的内容
	 * @param url
	 * @param reg    
	 * @author 张定国
	 * 2018年11月4日
	 */
	private void resolvUrl(String url,String tagnName,String reg){
		if(url.matches(REG_URL)) {
			try {
				//20秒超时连接
				Document doc = Jsoup.parse(new URL(url), 20000);
				resolveTag(doc, tagnName,reg);		
			} catch (IOException e) {
				System.err.println(url+":connect timeout!");
			}
		}
	}
	/**
	 * @Title resolveTag
	 * @Description 解析标签，内容
	 * @param doc 页面
	 * @param tagName 标签 
	 * @param reg 标签内容需要匹配的规则
	 * @author 张定国
	 * 2018年11月4日
	 * @param regEmail 
	 */
	private void resolveTag(Document doc,String tagName, String reg){
		for (Element element : doc.getElementsByTag(tagName)) {
			matcheContent(element.text(), reg);
		}
	}
	/**
	 * @Title matche
	 * @Description 内容匹配
	 * @param txt
	 * @param reg    
	 * @author 张定国
	 * 2018年11月4日
	 */
	private void matcheContent(String txt,String reg){
		if(txt.matches(reg)&&!content.contains(txt)) {
			content.add(txt);
		}
	}
}
