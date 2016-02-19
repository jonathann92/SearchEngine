package SearchEngine;

import java.io.Serializable;
import java.util.*;


public class Document implements Serializable {
	public class TFIDF implements Serializable{
		int termID;
		double tfidf;
		
		TFIDF(int termID, double tfidf){
			this.termID = termID;
			this.tfidf = tfidf;
		}
	}
	
	public String url;
	public String title;
	public String content;
	public int docID;
	public Map<Integer, Integer> wordFreq = new HashMap<Integer, Integer>();
	public List<TFIDF> vsm;
	
	public Document(){}
	
	public Document(String url, String title, String content, int docID, Map<Integer, Integer> wordFreq){
		this.url = url;
		this.title = title;
		this.content = content;
		this.docID = docID;
		this.wordFreq = wordFreq;
	}
	
	public String getURL(){
		return url;
	}
	
	public String getTitle(){
		return title;
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
	
	public List<TFIDF> getVSM(){
		return vsm;
	}
	
	public void setVSM(List<TFIDF> vsm){
		this.vsm = vsm;
	}
	
	@Override
	public String toString(){
		return "Title: " + this.title + "\nURL: " + this.url + "\nDocID: " + this.docID;
	}
}
