/*

Author : Renfei Chen
         HanTao Liu

*/
package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;
import java.util.Iterator;


/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.
    private IDictionary<URI, Double> documentNormValues;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.
        this.idfScores = this.computeIdfScores(webpages);
        this.documentNormValues = new ChainedHashDictionary();
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: Feel free to change or modify these methods if you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * This method should return a dictionary mapping every single unique word found
     * in any documents to their IDF score.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
    	Iterator<Webpage> itr = pages.iterator();
    	ISet<String> set = new ChainedHashSet();
    	IDictionary<String, Integer> map = new ChainedHashDictionary();
    	IDictionary<String, Double> result = new ChainedHashDictionary();
    	while (itr.hasNext()) {
    		IList<String> word = itr.next().getWords();
    		ISet<String> uniqueWord = new ChainedHashSet();
    		for (String s: word) {
    		    if (!uniqueWord.contains(s)) {
    		        if (map.containsKey(s)) {
                        map.put(s, map.get(s) + 1);
                    } else {
                        map.put(s, 1);
                        set.add(s);
                    }
    		        uniqueWord.add(s);
    		    }
    		}
    	}
    	Iterator<String> setItr = set.iterator();
    	while (setItr.hasNext()) {
    		String key = setItr.next();
    		result.put(key, Math.log((double) pages.size() / map.get(key)));
    	}
    	return result;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> wordMap = new ChainedHashDictionary(); 
        IDictionary<String, Integer> wordCount = new ChainedHashDictionary();
		ISet<String> keys = new ChainedHashSet();
		Iterator<String> itr = words.iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			if (wordCount.containsKey(key)) {
				wordCount.put(key, wordCount.get(key) + 1);
			} else {
				wordCount.put(key, 1);
				keys.add(key);
			}
		}
		Iterator<String> keysItr = keys.iterator();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			wordMap.put(key, (double) wordCount.get(key)/words.size());
		}		
		return wordMap;
    }

    /**
     * See spec for more details on what this method should do.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        Iterator<Webpage> itr = pages.iterator();
        IDictionary<URI, IDictionary<String, Double>> vectors = new ChainedHashDictionary();
        while (itr.hasNext()) {
        	Webpage page = itr.next();
        	IDictionary<String, Double> tfIdfScore = computeTfScores(page.getWords());
        	double sum = 0;
        	for (KVPair pair : tfIdfScore) {
        	    double tfs = tfIdfScore.get((String) pair.getKey());
        	    double idfs = idfScores.get((String) pair.getKey());
        	    double tfIdfValue = tfs * idfs;
        		tfIdfScore.put((String) pair.getKey(), tfIdfValue);
        		sum += tfIdfValue * tfIdfValue;
        		
        	}
        	vectors.put(page.getUri(), tfIdfScore);
        	this.documentNormValues.put(page.getUri(), Math.sqrt(sum));
        }
        return vectors;
    }
    

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // TODO: Replace this with actual, working code.

        // TODO: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> documentVector = this.documentTfIdfVectors.get(pageUri);
        
        /*for (KVPair now : documentVector) {
            System.out.println((String)now.getKey() + "       " + documentVector.get((String)now.getKey()));
        }*/
        //IDictionary<String, Double> queryVector = new ChainedHashDictionary(); // TF score
        IDictionary<String, Integer> wordCount = new ChainedHashDictionary();
        ISet<String> keys = new ChainedHashSet();
        Iterator<String> itr = query.iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            if (wordCount.containsKey(key)) {
                wordCount.put(key, wordCount.get(key) + 1);
            } else {
                wordCount.put(key, 1);
                keys.add(key);
            }
        }
        Iterator<String> keysItr = keys.iterator();
        double queryNormValue = 0;
        double docWordScore = 0;
        double numerator = 0;
        double denominator = 0;
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            double idfs = 0;
            if (this.idfScores.containsKey(key)) {
                idfs = this.idfScores.get(key);
            }
            double tfIdf = (double) wordCount.get(key) / query.size() * idfs;
            queryNormValue += tfIdf * tfIdf;
            if (documentVector.containsKey(key)) {
                docWordScore = documentVector.get(key);
            } else {
                docWordScore = 0;
            }
            numerator += docWordScore * tfIdf;
        }
        queryNormValue = Math.sqrt(queryNormValue);
        denominator = this.documentNormValues.get(pageUri) * queryNormValue;
        //System.out.println(queryNormValue + "      " + denominator);
        if (denominator != 0) {
            return numerator / denominator;
        } else {
            return 0.0;
        }
    }
}
