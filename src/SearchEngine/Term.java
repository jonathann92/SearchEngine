package SearchEngine;
import java.io.Serializable;
import java.util.List;

/*
 * Authors: Jonathan Nguyen 54203830
 * 				Gessica Torres 28808697
 * 				Leonard Bejosano 32437030
 */

@SuppressWarnings("serial")
public final class Term implements Serializable {
	String word;
	Integer df;
	Integer id;
	
	public Term(String word, Integer id){
		this.word = word;
		this.id = id;
	}
	
	public Term(String word, Integer df, Integer id){
		this.word = word;
		this.df = df;
		this.id = id;
	}
	
	public String getWord() {
		return word;
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(Integer id){
		this.id = id;
	}
	
	public void setDF(Integer df){
		this.df = df;
	}
	
	public int df(){
		return df;
	}
	
	@Override
	public String toString() {
		return id + ": " + word + ",";
	}
}
