package SearchEngine;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class Document implements Serializable {
	public String url;
	public String content;
	public int docID;
	public Map<Integer, Integer> wordFreq = new HashMap<Integer, Integer>();
	public List<Double> vsm;
	
	public Document(){}
	
	public Document(String url, int docID, Map<Integer, Integer> wordFreq){
		this.url = url;
		this.docID = docID;
		this.wordFreq = wordFreq;
	}
	
	public Document(String url, String content, int docID, Map<Integer, Integer> wordFreq){
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
	
	public Map<Integer, Integer> getWordFreq(){
		return wordFreq;
	}
	
	public List<Double> getVSM(){
		return vsm;
	}
	
	public void setVSM(List<Double> vsm){
		this.vsm = vsm;
	}
}
