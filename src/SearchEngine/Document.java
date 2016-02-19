package SearchEngine;

import java.io.Serializable;
import java.util.*;


@SuppressWarnings("serial")
public class Document implements Serializable {
	public class TFIDF{
		int termID;
		double tfidf;
		
		TFIDF(int termID, double tfidf){
			this.termID = termID;
			this.tfidf = tfidf;
		}
	}
	
	public String url;
	public String content;
	public int docID;
	public Map<Integer, Integer> wordFreq = new HashMap<Integer, Integer>();
	public List<TFIDF> vsm;
	
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
	
	public List<TFIDF> getVSM(){
		return vsm;
	}
	
	public void setVSM(List<TFIDF> vsm){
		this.vsm = vsm;
	}
}
