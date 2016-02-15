package SearchEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uci.ics.crawler4j.hw.Frequency;

public class Helper{
	
	public static List<Frequency> createFrequencies(Map<String, Integer> counter){
		List<Frequency> freqs = new ArrayList<Frequency>();

		for (Map.Entry<String,Integer> wordCounter : counter.entrySet()) {
	        String word = wordCounter.getKey();
	        Integer count = wordCounter.getValue();

	        freqs.add(new Frequency(word, count));
	    }

		return freqs;
	}
}