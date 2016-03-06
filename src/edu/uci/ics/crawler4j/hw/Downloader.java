package edu.uci.ics.crawler4j.hw;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Downloader {
	static final Pattern htmlPattern = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);
	static final String htmlDir = "/Users/Jonathan/storage/html/";
	
	public static Map<String, String> createFileURLMap(){
		Map<String, String> map = new HashMap<String, String>();
		try{
		String content = new Scanner(new File("html_files.json")).useDelimiter("\\Z").next();
		JSONObject jObject;

		jObject = new JSONObject(content.trim());
		
		Set<String> keys = jObject.keySet();
		for(String key: keys){

			String file = jObject.getJSONObject(key).getString("file");
			String url = jObject.getJSONObject(key).getString("url");
			map.put(file, url);
			
		}
		} catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}
	  
	  public static List<File> htmlFiles(){
		  List<File> htmlFiles = new ArrayList<File>();
		  File dir = new File(htmlDir);
		  if(!dir.exists() && !dir.isDirectory()){
			  System.out.println("Can't find HTML FILES OR IS NOT DIR");
			  System.exit(1);
		  }
		  int i = 0;
		  for(File file: dir.listFiles()){
			  if(file.getName().equals(".DS_Store")) continue;
			  if(i == 0)
				  System.out.println(file);
			  htmlFiles.add(file);
		  }
		  
		  
		  return htmlFiles;
	  }
	  
	  public static void main(String[] args){
		  
		  
		  Map<String, String> map = createFileURLMap();
		  List<File> files = htmlFiles();
		  int i = 0;
		  ObjectOutputStream oos = null;
		  try{
			  File output = new File("output.txt");
			  PrintWriter writer = new PrintWriter(output);
			  oos = new ObjectOutputStream(new FileOutputStream("PageData/pages.cwl"));
			  for(File file: files){
				  String name = file.getName();
				  String url = map.get(name);
					if(name.contains("&")) continue;
					try{
						Document doc = Jsoup.parse(file, "ISO-8859-1", url);
						Elements links = doc.select("a[href]");
						String title = doc.title();
						String text = doc.body().text();
						List<String> outlinks = new ArrayList<String>();
						
				        for (Element link : links) {
				        	String outlink = link.attr("abs:href");
				        
				        	if(outlink.contains("ics.uci.edu")){
				        	System.out.println("Link " + outlink);
				        	writer.println(outlink);
				        	}
				        }
				        
						oos.writeObject(new PageData(url, title, text, outlinks));
						} catch (Exception e3 ) { e3.printStackTrace(); }
				  }
			  oos.writeObject(null);
			  writer.close();
					

		  } catch (Exception e){
			  e.printStackTrace();
		  } finally {
			  if(oos != null){
				  try { oos.close(); } catch (Exception e2) { e2.printStackTrace(); }
			  }
		  }
		  System.out.println("I: " + i);
		  
		  
	  }
}
