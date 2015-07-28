package com.couchbase.support;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;

public class SimpleBucketFiller {

	public static void main(String[] args) {

		// These control how much informational output is displayed
		// You can suppress the printing of the generated json docs if you wish
		boolean	printEachDocument	= true;
		boolean showYourWork        = true;
		
		// Please replace these with the values from your cluster and bucket
		String HOSTNAME           = "192.168.42.101";
		String BUCKETNAME         = "BUCKETNAME";
		
		// Number of documents to try to insert
		int    NUMDOCUMENTS       = 1000;		

		// You can set this to true if you wish to have randomly set document
		// expirations, or false if you wish documents to have no expiration
		boolean randomExpiry = true;		

		// Maximum expiration time from now, in seconds
		// Only used if randomExpiry is true
		// 1000000 seconds is about 12 days
		int    HIGHEXPIRATION     = 60;	

		// You can set this to true for random doc sizes or false for more of a static doc
		boolean randomSizes  = true;

		// You may set this to any value you like
		String DOCUMENTNAMEPREFIX = "sbfDocument";

		// If randomSizes is true, will create documents that randomly have
		// a size in this range
		int		MINDOCSIZE	= 10;
		int 	MAXDOCSIZE	= 20;
		
		int		expiryRange = MAXDOCSIZE - MINDOCSIZE;
		
		String jsonDocumentString = "";
		String documentKey        = "";
		String randomLetters      = "";
		
		JsonObject   jsonObject   = null;
		JsonDocument jsonDocument = null;

		int sizeOfThisDocument  = 0;
		int docExpiry           = 0;
		int docRandomSize       = 0;
		int alreadyExistCount   = 0;
		int otherExceptionCount = 0;
		int successfulInsert    = 0;
		
		// Keep track of these for successful inserts only
		int minGeneratedSize    = 100000000;   // an unlikely small size
		int maxGeneratedSize    = 0;           // an unlikely large size
		int cumulativeDocSize   = 0;           // sum total of all json doc strings
		char randomChar			= 0;
		
		// Connect to the cluster
		Cluster cluster = CouchbaseCluster.create(HOSTNAME);
		Bucket bucket = cluster.openBucket(BUCKETNAME);	
		
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < NUMDOCUMENTS; i++) {
				
			// create a document
			documentKey = DOCUMENTNAMEPREFIX + i;
			
			if (randomSizes) {
				docRandomSize = ((int) (Math.random() * expiryRange)) + MINDOCSIZE;
								
				randomLetters = "";
				for (int x = 0; x < docRandomSize; x++) {
					randomChar = (char) (((int) (Math.random() * (90 - 65)) + 65));	// a random ascii char between 65 and 90
					randomLetters = randomLetters + randomChar;
				}
				jsonDocumentString = "{  \"randomData\" : \"" + randomLetters + "\", \"name\" : \"" + documentKey +  "\", \"serialNumber\" : " + i + " }";
			} else {
				jsonDocumentString = "{  \"body\" : \"This is some document data\", \"name\" : \"" + documentKey + "\", \"serialNumber\" : " + i + " }";
			}
						
			sizeOfThisDocument = jsonDocumentString.length();
			
			if (printEachDocument) { System.out.println(jsonDocumentString + " Length: " + sizeOfThisDocument); }
			
			// Create the JSON Object from the generated json document string
			jsonObject = JsonObject.fromJson(jsonDocumentString);

			// Determine docExpiry based on the options
			if (randomExpiry) {
				docExpiry = (int) (Math.random() * HIGHEXPIRATION);
			}
			else {
				docExpiry = 0;
			}
			
			if (showYourWork) {
				System.out.println("Working on document #" + i + " of " + NUMDOCUMENTS + ". Using expiration of " + docExpiry + " seconds");
			}
			
			try {
				jsonDocument = JsonDocument.create(documentKey, docExpiry, jsonObject);

				// insert the document
				bucket.insert(jsonDocument);			
			
				// If we got here, insert was successful.  Do some bookkeeping.
				successfulInsert++;
				cumulativeDocSize = cumulativeDocSize + sizeOfThisDocument;
				if (sizeOfThisDocument < minGeneratedSize) { minGeneratedSize = sizeOfThisDocument; }
				if (sizeOfThisDocument > maxGeneratedSize) { maxGeneratedSize = sizeOfThisDocument; }
				
			} catch (DocumentAlreadyExistsException daee) {
				alreadyExistCount++;
			} catch (Exception e) {
				otherExceptionCount++;
			}			
			
		} // for each document
		
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		
		long docsPerSecond = ((NUMDOCUMENTS * 1000)/ elapsedTime);
		
		System.out.println("Done.  Successfully inserted:         " + successfulInsert + " documents.");
		System.out.println("# of documents that already existed:  " + alreadyExistCount);
		System.out.println("Number of other exceptions:           " + otherExceptionCount);
		System.out.println("Total run time was:                   " + elapsedTime + " millseconds");
		System.out.println("Performance was about:                " + docsPerSecond + " docs per second");
		System.out.println("Total successful doc size was:        " + cumulativeDocSize + " bytes");
		System.out.println("Generated doc size range was between  " + minGeneratedSize + " and " + maxGeneratedSize + " bytes");
				
	}

}
