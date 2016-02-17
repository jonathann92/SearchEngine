package edu.uci.ics.crawler4j.hw;

import java.io.Serializable;

public class PageData implements Serializable{
	String title = null;
	String url = null;
	String text = null;

	public PageData(){}

	public PageData(String url, String title, String text){
		this.title = title;
		this.url = url;
		this.text = text;
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

	@Override
	public String toString(){
		return "Title: " + this.title + "\nURL: " + this.url + "\nText length: " + this.text.length();
	}
}
