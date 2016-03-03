package SearchEngine;

import java.io.Serializable;
import java.util.*;


@SuppressWarnings("serial")
public class Document implements Comparable<Document>, Serializable {
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
	public int id;
	public int rank;
	public Map<Integer, Integer> wordFreq = new HashMap<Integer, Integer>();
	public List<TFIDF> vsm;
	
	public Document(){}
	
	public Document(String url, String title, String content, int docID, Map<Integer, Integer> wordFreq){
		this.url = url;
		this.title = title;
		this.content = content;
		this.id = docID;
		this.wordFreq = wordFreq;
	}
	
	public Document(Document d, int rank){
		this.url = d.url;
		this.title = d.title;
		this.content = d.content;
		this.id = d.id;
		this.rank = rank;
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
	
	public int getID(){
		return id;
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
		return "Title: " + this.title + "\nURL: " + this.url + "\nDocID: " + this.id;
	}
	
	@Override
	public int compareTo(Document d) {
		return this.id - d.id;
	}
}
