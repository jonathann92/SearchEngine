package SearchEngine;

import java.util.List;

public class test {

	public static void main(String[] args) {
		List<Document> docs = (List<Document>)Helper.readObjectFromFile("rankeddocs");
		
		for(Document d : docs)
			if(d.getRank() >= 1.0)
				System.out.println(d.getUrl() + " : " + d.getRank());

	}

}
