package SearchEngine;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class Document implements Serializable {
	String url;
	String content;
	int docID;
	Map<String, Integer> wordFreq = new HashMap<String, Integer>();
	List<Double> vsm;
	
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
