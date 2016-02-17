package edu.uci.ics.crawler4j.hw;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Downloader {
	
	
	public static Map<String, String> createFileURLMap(){
		Map<String, String> map = new HashMap<String, String>();
		try{
		String content = new Scanner(new File("html_files.json")).useDelimiter("\\Z").next();
		JSONObject jObject;

		jObject = new JSONObject(content.trim());
		
		int i = 0;
		Set<String> keys = jObject.keySet();
		for(String key: keys){
			if(i == 0){
			String file = jObject.getJSONObject(key).getString("file");
			String url = jObject.getJSONObject(key).getString("url");
			map.put(file, url);
			}
		}
		} catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}
	  
	  public static List<String> htmlFiles(){
		  List<String> htmlFiles = new ArrayList<String>();
		  File dir = new File("./Html");
		  if(!dir.exists() && !dir.isDirectory()){
			  System.out.println("Can't find HTML FILES OR IS NOT DIR");
			  System.exit(1);
		  }
		  int i = 0;
		  for(File file: dir.listFiles()){
			  if(file.getName().equals(".DS_Store")) continue;
			  if(i++ == 0)
				  System.out.println(file.getName());
			  htmlFiles.add(file.getName());
		  }
		  
		  
		  return htmlFiles;
	  }
	  
	  public static void main(String[] args){
		  Map<String, String> map = createFileURLMap();
		  List<String> files = htmlFiles();
		  int i = 0;
		  ObjectOutputStream oos = null;
		  try{
			  oos = new ObjectOutputStream(new FileOutputStream("PageData/pages.cwl"));
				  
			  for(String file: files){
				  String pathToFile = "Html/" + file;
				  
						String content = new Scanner(new File(pathToFile)).useDelimiter("\\Z").next();

						try{
						Document doc = Jsoup.parse(content);
						String title = doc.title();
						String text = doc.body().text();
						System.out.println(map.get(file));
						System.out.println(title);
						System.out.println(text);
						oos.writeObject(new PageData(map.get(file), title, text));
						} catch (Exception e3 ) { e3.printStackTrace(); }
			  }
			  oos.writeObject(null);
					

		  } catch (Exception e){
			  e.printStackTrace();
		  } finally {
			  if(oos != null){
				  try { oos.close(); } catch (Exception e2) { e2.printStackTrace(); }
			  }
		  }
		  
		  
	  }

	  
}
