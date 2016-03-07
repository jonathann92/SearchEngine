package SearchEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class Engine{
	
	final static double titleWeight = .7;
	final static double tfidfWeight = .3;
	
	public static class docScore implements Comparable<docScore>{
		public double score;
		public int id; // document id
		
		docScore(double score, int id){
			this.score = score;
			this.id = id;
		}
		
		@Override
		public int compareTo(docScore b) {
			Double bScore = b.score;
			Double aScore = this.score;
			return bScore.compareTo(aScore);
		}
		
		@Override
		public String toString(){
			return "DocID: " + id + "\nScore: " + score;
		}
		
	}
	
	public static List<docScore> search(List<Document> docs, List<Term> terms, Map<String, Integer> t2tid, List<String> query){
		List<docScore> results = new ArrayList<docScore>();
		double corpus = docs.size();
		
		for(Document d : docs){
			if(d.getUrl().contains("?") || d.getTitle().contains("Index of")) continue;
			double tfidfScore = tfidfScore(terms, t2tid, query, corpus, d);
			double titleScore = titleScore(d, query);
			double score = titleWeight * titleScore + tfidfWeight * tfidfScore;
			//score *= d.getRank();
			
			if(score > 0){
				results.add(new docScore(score, d.getId()));
			}
		}
		
		Collections.sort(results);
		return results;
	}
	
	private static double titleScore(Document page, List<String> query){
		double score = 0.0;
		Set<String> title = new HashSet<String>(Arrays.asList(page.getTitle().toLowerCase().split("[^A-Za-z0-9]")));
		for(String word : query){
			if(title.contains(word))
				++score;
		}
		
		return score/query.size();
	}

	private static double tfidfScore(List<Term> terms, Map<String, Integer> t2tid, List<String> query, double corpus,
			Document d) {
		double tfidfScore = 0.0;
		
		for(String word : query){
			Integer termID = t2tid.get(word);
			Integer tf = d.getWordFreq().get(termID);
			
			if(tf != null) 
				tfidfScore += (1 + Math.log10(tf)) * Math.log10(corpus/ terms.get(termID).df());
		}
		return tfidfScore;
	}
	
	@SuppressWarnings("resource")
	public static List<String> grabQuery(){
		System.out.print("Enter Query: ");
		Scanner in = new Scanner(System.in);
		String line = in.nextLine();
		String input[] = line.toLowerCase().split("\\s+");
		List<String> query = Arrays.asList(input);
		return query;
	}
	
	public static void printResults(List<docScore> results, List<Document> docs){
		for(int i = 0; i < 10 && i < results.size(); ++i){
			int id = results.get(i).id;
			Document page = docs.get(id);
			
			System.out.println("Title: " + page.getTitle());
			System.out.println("URL: " + page.getUrl());
			System.out.println();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		List<Document> docs = (List<Document>) Helper.readObjectFromFile("rankeddocs");
		List<Term> terms = (List<Term>) Helper.readObjectFromFile("terms");
		Map<String, Integer> t2tid = (Map<String, Integer>) Helper.readObjectFromFile("t2tidMap");
		
		while(true){
			System.out.println("***********************");
			System.out.println("***********************");
			System.out.println("***********************");
			List<String> query = grabQuery();
			System.out.println(query);
			List<docScore> searchResults = search(docs, terms, t2tid, query);
			System.out.println(searchResults.size());
			printResults(searchResults, docs);
		}
		
		
	}
}