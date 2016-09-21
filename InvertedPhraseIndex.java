package ir.vsr;

import java.io.*;
import java.util.*;
import java.lang.*;

import ir.utilities.*;
import ir.classifiers.*;

public class InvertedPhraseIndex extends InvertedIndex{

   private int maxPhrases;

   public InvertedPhraseIndex(File dirFile, short docType, boolean stem, boolean feedback, int maxPhrases){
      //override indexDocuments to avoid indexing before running bigram changes
      super();
      this.dirFile = dirFile;
      this.docType = docType;
    this.stem = stem;
    this.feedback = feedback;
    tokenHash = new HashMap<String, TokenInfo>();
    docRefs = new ArrayList<DocumentReference>();
      this.maxPhrases = maxPhrases;
      indexDocuments();

   }

   public void indexDocuments(){
   //ok. before indexing documents, we need to calculate frequencies and find list of common phrases.
      DocumentIterator docIterator = new DocumentIterator(dirFile, docType, stem);
      HashMap<String, Integer> phraseFrequencies = new HashMap<String, Integer>();
   //Use docIterator to iterate through documents.
      while(docIterator.hasMoreDocuments()){
         //get a document
         FileDocument doc = docIterator.nextDocument();
         //iterate through tokens
         String firstToken = "";
         while(doc.hasMoreTokens()){
            String secondToken = doc.nextToken();
            if(!firstToken.equals("") && !secondToken.equals("")){
               if(!phraseFrequencies.containsKey(firstToken + secondToken))
                  phraseFrequencies.put(firstToken + secondToken, 1);
               else
                  phraseFrequencies.put(firstToken + secondToken, phraseFrequencies.get(firstToken + secondToken) + 1);

            }
            firstToken = secondToken;
         }


//comment
      }
      System.out.println(phraseFrequencies.toString());

   }

   public static void main(String[] args){

    String dirName = args[args.length - 1];
    short docType = DocumentIterator.TYPE_TEXT;
    boolean stem = false, feedback = false;
    for (int i = 0; i < args.length - 1; i++) {
      String flag = args[i];
      if (flag.equals("-html"))
        // Create HTMLFileDocuments to filter HTML tags
        docType = DocumentIterator.TYPE_HTML;
      else if (flag.equals("-stem"))
        // Stem tokens with Porter stemmer
        stem = true;
      else if (flag.equals("-feedback"))
        // Use relevance feedback
        feedback = true;
      else {
        throw new IllegalArgumentException("Unknown flag: "+ flag);
      }
    }


    // Create an inverted index for the files in the given directory.
    InvertedPhraseIndex index = new InvertedPhraseIndex(new File(dirName), docType, stem, feedback, 1000);

      
   }

}
