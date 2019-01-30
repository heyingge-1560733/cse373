package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

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
    private IDictionary<URI, Double> normDocumentVector;
    
    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    
    // Feel free to add extra fields and helper methods.
    
    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.
        this.normDocumentVector = new ChainedHashDictionary<URI, Double>();
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        for (KVPair<URI, IDictionary<String, Double>> pair : this.documentTfIdfVectors) {
            normDocumentVector.put(pair.getKey(), norm(pair.getValue()));
        }
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
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> idf = new ChainedHashDictionary<String, Double>();
        int docNum = pages.size();
        for (Webpage page : pages) {
            ISet<String> wordSet = new ChainedHashSet<String>();
            for (String word : page.getWords()) {
                wordSet.add(word);
            }
            for (String word : wordSet) {
                String nextWord = word;
                if (idf.containsKey(nextWord)) {
                    double denominator = (double) docNum / Math.exp(idf.get(nextWord)) + 1.0;
                    idf.put(nextWord, Math.log((double) docNum / denominator));
                } else {
                    idf.put(nextWord, Math.log((double) docNum));
                }
            }
        }
        return idf;
    }
    
    /**
    * Returns a dictionary mapping every unique word found in the given list
    * to their term frequency (TF) score.
    *
    * We are treating the list of words as if it were a document.
    */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> result = new ChainedHashDictionary<String, Double>();
        for (String word : words) {
            if (result.containsKey(word)) {
                result.put(word, result.get(word) + 1.0 / words.size());
            } else {
                result.put(word, 1.0 / words.size());
            }
        }
        return result;
    }
    
    /**
    * See spec for more details on what this method should do.
    */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        //throw new NotYetImplementedException();
        IDictionary<URI, IDictionary<String, Double>> vectors =
        new ChainedHashDictionary<URI, IDictionary<String, Double>>();
        for (Webpage page : pages) {
            IDictionary<String, Double> tfScores = computeTfScores(page.getWords());
            IDictionary<String, Double> temp = new ChainedHashDictionary<String, Double>();
            for (KVPair<String, Double> pair : tfScores) {
                temp.put(pair.getKey(), pair.getValue() * this.idfScores.get(pair.getKey()));
            }
            vectors.put(page.getUri(), temp);
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
    public Double computeRelevance(IList<String> query, URI pageUri) {
        IDictionary<String, Double> documentVector = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryTf = computeTfScores(query);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<String, Double>();
        double numerator = 0.0;
        for (String word : query) {
            double docWordScore = 0.0;
            if (idfScores.containsKey(word)) {
                queryVector.put(word, queryTf.get(word) * this.idfScores.get(word));
            } else {
                queryVector.put(word, 0.0);
            }
            if (documentVector.containsKey(word)) {
                docWordScore = documentVector.get(word);
            }
            double queryWordScore = queryVector.get(word);
            numerator += docWordScore * queryWordScore;
        }
        double denominator = normDocumentVector.get(pageUri) * norm(queryVector);
        if (denominator != 0.0) {
            return numerator / denominator;
        } else {
            return 0.0;
        }
    }
    
    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair : vector) {
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}