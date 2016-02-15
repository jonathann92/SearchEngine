package SearchEngine;

import java.util.HashMap;
import java.util.Map;

public class Document{
	String url;
	String content;
	int docID;
	Map<String, Integer> wordFreq = new HashMap<String, Integer>();
	
	public Document(){}
	
	public Document(String url, int docID, Map<String, Integer> wordFreq){
		this.url = url;
		this.docID = docID;
		this.wordFreq = wordFreq;
	}
	
	public Document(String url, String content, int docID, Map<String, Integer> wordFreq){
		this.url = url;
		this.content = content;
		this.docID = docID;
		this.wordFreq = wordFreq;
	}
	
	public String getURL(){
		return url;
	}
	
	public String getContent(){
		return content;
	}
	
	public int getDocID(){
		return docID;
	}
	
	public Map<String, Integer> getWordFreq(){
		return wordFreq;
	}
}
