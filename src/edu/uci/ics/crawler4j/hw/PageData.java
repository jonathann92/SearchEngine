package edu.uci.ics.crawler4j.hw;

import java.io.Serializable;
import java.util.Set;

public class PageData implements Serializable{
	String title = null;
	String url = null;
	String text = null;
	Set<String> outlinks = null;

	public PageData(){}

	public PageData(String url, String title, String text, Set<String> outlinks){
		this.title = title;
		this.url = url;
		this.text = text;
		this.outlinks = outlinks;
	}

	public String getURL(){
		return this.url;
	}

	public String getText(){
		return this.text;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public Set<String> getOutlinks(){
		return this.outlinks;
	}

	@Override
	public String toString(){
		return "Title: " + this.title + "\nURL: " + this.url + "\nText length: " + this.text.length();
	}
}
