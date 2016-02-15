package SearchEngine;
import java.io.Serializable;

/*
 * Authors: Jonathan Nguyen 54203830
 * 				Gessica Torres 28808697
 * 				Leonard Bejosano 32437030
 */

public final class Term implements Serializable {
	String word;
	int id;
	
	
	public Term(String word, int frequency, int id){
		this.word = word;
		this.id = id;
	}
	
	public Term(String word) {
		this.word = word;
	}
	
	public Term(String word, int frequency) {
		this.word = word;
	}
	
	public String getText() {
		return word;
	}
	
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id + ": " + word + ",";
	}
}
