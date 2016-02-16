package SearchEngine;

import java.util.*;

public class Document{
	//String title; soon to be added
	String url;
	String content;
	int docID;
	Map<String, Integer> wordFreq = new HashMap<String, Integer>();
	Set<String> words;
	List<Double> vsm;
	
	public Document(){}
	
	public Document(String url, int docID, Map<String, Integer> wordFreq, Set<String> words){
		this.words = words;
		this.url = url;
		this.docID = docID;
		this.wordFreq = wordFreq;
	}
	
	public Set<String> words(){
		return words;
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
	
	public int ID(){
		return docID;
	}
	
	public Map<String, Integer> getWordFreq(){
		return wordFreq;
	}
	
	public List<Double> getVSM(){
		return vsm;
	}
	
	public void setVSM(List<Double> vsm){
		this.vsm = vsm;
	}
}
