package SearchEngine;

import java.util.List;
import java.util.Set;

public class PageRank {
	

	public static void main(String[] args) {
		List<Document> docs = (List<Document>) Helper.readObjectFromFile("docs");
		initialize(docs);
		rankPages(docs);
		
//		for(int i = 0; i < docs.size(); ++i)
//			System.out.println(i + " : " + docs.get(i).getRank());
		
		int count = 0;
		for(Document d : docs){
			if(d.getRank() <= 0.0){
				++count;
				//System.out.println(d.getUrl());
				d.setRank(.05);
			}
		}
		System.out.println(count);
		Helper.writeObjectToFile(docs, "rankeddocs");
		
		
	}
	
	private static void rankPages(List<Document> docs){
		double convergence = Double.MAX_VALUE;
		double total = docs.size();
		while(convergence > 1.0){
			double oldTotal = total;
			convergence = 0.0;
			for(Document d : docs){
				double prevRank = d.getRank();
				double newRank = calculateRank(docs, d);
				d.setRank(newRank);
				convergence += Math.abs(prevRank - newRank);
				
			}
			
			total = 0.0;
			for(Document d : docs){
				total += d.getRank();
			}
			
			System.out.println("SUM: " + total);
			System.out.println("CON: " + convergence);
		}
	}

	private static double calculateRank(List<Document> docs, Document d) {
		Set<Integer> inlinks = d.getInlinks();
		double newRank = 0.0;
		for(Integer in : inlinks){
			newRank += (docs.get(in).getRank() / docs.get(in).outlinks);
		}
		
		return newRank;
	}
	

	private static void initialize(List<Document> docs) {
		for(Document d : docs)
			d.setRank(1.0);
		
		docs.get(10000).setRank(1.0);
	}

}
