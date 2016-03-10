
package SearchEngine;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres 28808697
 * 			Leonard Bejosano 32437030
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import SearchEngine.Term;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import edu.uci.ics.crawler4j.hw.*;

public class ProcessData {

	public static Set<String> setStopWords(){
		String words = "a about above after again against all am an "
				+ "and any are aren't as at be because been before being "
				+ "below between both but by can't cannot could couldn't did didn't "
				+ "do does doesn't doing don't down during each few for from further "
				+ "had hadn't has hasn't have haven't having he he'd he'll he's her here "
				+ "here's hers herself him himself his how how's i i'd i'll i'm i've if "
				+ "in into is isn't it it's its itself let's me more most mustn't my myself "
				+ "no nor not of off on once only or other ought our ours ourselves out "
				+ "over own same shan't she she'd she'll she's should shouldn't so some such "
				+ "than that that's the their theirs them themselves then there there's these "
				+ "they they'd they'll they're they've this those through to too under until up "
				+ "very was wasn't we we'd we'll we're we've were weren't what what's when when's "
				+ "where where's which while who who's whom why why's with won't would wouldn't you "
				+ "you'd you'll you're you've your yours yourself yourselves";

		return new HashSet<String>(Arrays.asList(words.replaceAll("'", "").split("\\s+")));
	}

	public static Set<String> uniqueWords(List<PageData> pages){
		Set<String> unique = new HashSet<String>();
		Set<String> stopWords = setStopWords();

		for(PageData page : pages){
			unique.addAll(Arrays.asList(page.getText().replaceAll("'", "").toLowerCase().split("[^A-Za-z0-9`']")));
		}
		unique.remove("");
		for(Iterator<String> it = unique.iterator(); it.hasNext(); ){
			String word = it.next();
			if(Character.isDigit(word.charAt(0)) || word.length() < 2)
				it.remove();
		}

		unique.removeAll(stopWords);
		return unique;
	}

	public static List<PageData> getFilesInDirectory(String dir){
		List<String> toReturn = new ArrayList<String>();

		File folder = new File(dir);

		for(File file: folder.listFiles()){
			String path = file.getAbsolutePath();

			if(path.endsWith(".cwl")){
				toReturn.add(file.getAbsolutePath());
			System.out.println(file.getAbsolutePath());
			}
		}

		return getDataFromFiles(toReturn);
	}

	public static List<PageData> getDataFromFiles(List<String> files){
		List<PageData> toReturn = new ArrayList<PageData>();

		for(int i = 0; i < files.size(); ++i){
			ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream(files.get(i)));
				PageData data = null;
				while((data = (PageData) ois.readObject()) != null)
					toReturn.add(data);
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				try {
					if(ois != null)
						ois.close(); }
				catch (IOException e2 ) { }
			}
		}

		return toReturn;
	}

	public static Integer countDF(Integer id, List<Document> docs){
		Integer count = 0;

		for(Document document: docs){
			if(document.getWordFreq().containsKey(id))
				++count;
		}

		return count;
	}

	public static List<Term> createTerms(List<Document> docs, Map<String, Integer> t2tid){
		System.out.println("Creating Terms");
		List<Term> terms = new ArrayList<Term>();

		int i = 0;
		for(String word: t2tid.keySet()){
			if(++i % 1000 == 0)
				System.out.println(++i);
			Integer id = t2tid.get(word);
			Integer df = countDF(id, docs);
			terms.add(new Term(word, df, id));
		}
		return terms;
	}

	
	public static void inlinks(List<Document> docs, Map<String, Integer> docMap, Set<Integer> outlinks, int id){
		for(Integer targetDoc : outlinks){
			Document target = docs.get(targetDoc);
			target.addInlink(id);
		}
	}

	private static Set<Integer> outlinkSet(Map<String, Integer> docMap, List<Set<String>> outlinks, int i) {
		Set<Integer> out = new HashSet<Integer>();
		Set<String> linkSet = outlinks.get(i);
		Set<String> remove = new HashSet<String>();
		
		for(String s : linkSet){
			if(docMap.get(s) == null){
				remove.add(s);
			}
		}
		
		linkSet.removeAll(remove);
		
		for(String s : linkSet){
			if(docMap.get(s) == null)
				System.out.println("WTF");
			else
				out.add(docMap.get(s));
		}
		
		return out;
	}
	
	public static void documentLinks(List<Document> docs, Map<String, Integer> docMap, List<Set<String>> outlinks){
		for(int i = 0; i < docs.size(); ++i){
			Document d = docs.get(i);
			
			Set<Integer> out = outlinkSet(docMap, outlinks, i);
			inlinks(docs, docMap, out, d.getId());
			d.setOutlinks(out.size());
		}
	}
	
	public static Map<String, Integer> makeDocMap(List<Document> docs){
		Map<String, Integer> toReturn = new HashMap<String, Integer>();
		
		for(Document d : docs){
			toReturn.put(d.getUrl(), d.getId());
		}
		
		return toReturn;
	}

	public static List<Document> createDocuments(List<PageData> pages, Map<String, Integer> t2tid){
		List<Document> documents = new ArrayList<Document>();
		Set<String> stopWords = setStopWords();
		List<Set<String>> outlinks = new ArrayList<Set<String>>();

		int i = 0;
		for(PageData page : pages){
			System.out.println(++i);

			Map<Integer, Integer> termFreq = new HashMap<Integer, Integer>();
			List<String> text = Arrays.asList(page.getText().replaceAll("'", "").toLowerCase().split("[^A-Za-z0-9`']"));
			Set<String> uniquePage = new HashSet<String>(text);
			uniquePage.remove("");
			uniquePage.removeAll(stopWords);

			for(String word : uniquePage){
				if(word.length() < 2 || Character.isDigit(word.charAt(0))) continue;
				Integer termID = t2tid.get(word);

				termFreq.put(termID, Collections.frequency(text, word));
			}
			String title = page.getTitle();
			if(title == null)
				title = "";
			documents.add(new Document(page.getURL(), title, page.getText(), documents.size(), termFreq));
			outlinks.add(page.getOutlinks());
		}
		
		Map<String, Integer> docMap = makeDocMap(documents);
		documentLinks(documents, docMap, outlinks);

		return documents;
	}

	public static List<PageData> getPages(String dir){
		@SuppressWarnings("unchecked")
		List<PageData> pages = (List<PageData>) Helper.readObjectFromFile("pages");
		if(pages == null){
			pages = getFilesInDirectory(dir+"PageData"+File.separator);
			Helper.writeObjectToFile((Object) pages, "pages");
		}

		return pages;
	}

	public static List<Document> getDocuments(List<PageData> pages, Map<String, Integer> t2tid){
		@SuppressWarnings("unchecked")
		List<Document> docs = (List<Document>) Helper.readObjectFromFile("docs");
		if(docs == null){
			docs = createDocuments(pages, t2tid);
			Helper.writeObjectToFile((Object) docs, "docs");
		}
		Collections.sort(docs);
		return docs;
	}

	public static List<Term> getTerms(List<Document> docs, Map<String, Integer> t2tid){
		@SuppressWarnings("unchecked")
		List<Term> terms = (List<Term>) Helper.readObjectFromFile("terms");
		if(terms == null){
			terms = createTerms(docs, t2tid);
			Helper.writeObjectToFile((Object) terms, "terms");
		}
		Collections.sort(terms);
		return terms;
	}

	/*

	public static void docid2termlist(List<Term> terms, List<Document> docs){
		System.out.print("{");
		int i = 0;
		for(Document doc: docs){
			if(i++ > 20) break;
			System.out.print(doc.id+": [");
			int k = 0;
			for(Integer key : doc.getWordFreq().keySet()){
				if(k >= 20) break;
				System.out.print(key + ", ");
			}
			System.out.println("],");
		}
	}

	public static void term2termid(Map<String, Integer> map){
		System.out.print("{ " );
		int i = 0;
		for(String key: map.keySet()){
			if(i++ >= 100) break;

			System.out.print(key + ":" + map.get(key) + ", ");
		}
		System.out.println("}");
	}

	public static void termid2term(Map<String, Integer> map){
		System.out.print("{ " );
		int i = 0;
		for(String key: map.keySet()){
			if(i++ >= 100) break;

			System.out.print(map.get(key) + ":" + key + ", ");
		}
		System.out.println("}");
	}

	public static void termFrequency(List<Term> terms, List<Document> docs){
		int i = 0;
		for(Term term: terms){
			if(i++ >= 100) break;

			System.out.print(term.getID() + ":[");


			for(int k = 0; k < 100; ++k){
				System.out.print("{");
				Document d = docs.get(k);
				Map<Integer, Integer> map = d.getWordFreq();
				System.out.print(d.getID() + ": " + map.get(term.getID()) + "}, ");
			}
			System.out.print("]");
		}
	}

	public static void tfidf(List<Term> terms, List<Document> docs){
		int i = 0;
		double corpus = docs.size();
		PrintWriter outfile = null;
		try{
			for(Document d : docs){
				List<TFIDF> vsm = new ArrayList<TFIDF>();
				if(i++ % 1000 == 0)
					System.out.println(i);
				for(Term t : terms){
					Integer tf = d.getWordFreq().get(t.getID());
					Double num = 0.0;
					if(tf != null){
						num = (1 + Math.log10(tf)) * Math.log10(corpus/ t.df());
						TFIDF tfidf = d.new TFIDF(t.getID(), num);
						vsm.add(tfidf);
					}
				}
				d.setVSM(vsm);
			}


//			outfile = new PrintWriter("tfidf.txt");
//			for(Term t : terms){
//				List<Double> vsm = new ArrayList<Double>();
//				outfile.print(i + ": [ ");
//				System.out.println(++i);
//				int k = 0;
//				for(Document d: docs){
//					Integer tf = d.getWordFreq().get(t.getID());
//					Double num = 0.0;
//					if(tf != null){
//						num = (1 + Math.log10(tf)) * Math.log10(corpus/ t.df());
//						outfile.print((k++) + ":" + num + " ");
//					}
//				}
//				outfile.println("]");
//			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(outfile != null)
				try { outfile.close(); } catch (Exception ee) { ee.printStackTrace(); }
		}
	}
	*/

	public static Map<String, Integer> makeTermID(List<PageData> pages){
		@SuppressWarnings("unchecked")
		Map<String, Integer> t2tid = (Map<String, Integer>) Helper.readObjectFromFile("t2tidMap");
		if(t2tid == null){
			Set<String> unique = uniqueWords(pages);
			t2tid = new HashMap<String, Integer>();
			for(String word : unique)
				t2tid.put(word, t2tid.size());
			Helper.writeObjectToFile(t2tid, "t2tidMap");
		}

		return t2tid;
	}

	public static void check(List<Term> terms, Map<String, Integer> t2tid){
		System.out.println("Checking Terms");
		for(Term t : terms){
			if(t.getID() != t2tid.get(t.getWord()))
				System.out.println("Not mapped correctly");
		}
	}

	public static void process(String dir){
		/* Loading Data */
		List<PageData> pages = getPages(dir);
		//List<PageData> pages = null;


		Map<String, Integer> t2tid = makeTermID(pages);
		List<Document> docs = getDocuments(pages, t2tid);
		Document d = docs.get(0);
		System.out.println(d);
		System.out.println(d.getTitle());
		List<Term> terms = getTerms(docs, t2tid);
		// /* End Loading Data */
		check(terms, t2tid);
	}

	public static void main(String[] args) {
		// SETUP
		if(args.length != 1) {
			System.out.println("Need 1 argument");
			return;
		}
		System.out.println("RUN");
		String dir = args[0].endsWith(File.separator) ? args[0] : (args[0]+=File.separator);
		process(dir);
		System.out.println("DONE");
	}
}
