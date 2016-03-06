package SearchEngine;

import java.io.Serializable;
import java.util.*;


@SuppressWarnings("serial")
public class Document implements Comparable<Document>, Serializable {
	
	public String url;
	public String title;
	public String content;
	public int id;
	public double rank;
	public Map<Integer, Integer> wordFreq = new HashMap<Integer, Integer>();
	
	public Document(){}
	
	public Document(String url, String title, String content, int docID, Map<Integer, Integer> wordFreq){
		this.url = url;
		this.title = title;
		this.content = content;
		this.id = docID;
		this.wordFreq = wordFreq;
		this.rank = 0.0;
	}
	
	public Document(Document d, int rank){
		this.url = d.url;
		this.title = d.title;
		this.content = d.content;
		this.id = d.id;
		this.rank = rank;
	}
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	public Map<Integer, Integer> getWordFreq() {
		return wordFreq;
	}

	public void setWordFreq(Map<Integer, Integer> wordFreq) {
		this.wordFreq = wordFreq;
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
