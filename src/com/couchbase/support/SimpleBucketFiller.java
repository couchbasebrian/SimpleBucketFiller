package com.couchbase.support;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;

public class SimpleBucketFiller {

	public static void main(String[] args) {

		String HOSTNAME           = "10.4.2.121";
		String BUCKETNAME         = "histogramtest";
		int    NUMDOCUMENTS       = 1000;		// Create 1000 documents
		int    HIGHEXPIRATION     = 10000;	// 1000000 seconds is about 12 days
		
		// The settings above will create 1000 documents which expire in the 
		// next 0-1,000,000 seconds from now.
		
		String DOCUMENTNAMEPREFIX = "documentT";
		String jsonDocumentString = "";
		String documentKey        = "";
		JsonObject jsonObject     = null;

		// Connect to the cluster
		Cluster cluster = CouchbaseCluster.create(HOSTNAME);
		Bucket bucket = cluster.openBucket(BUCKETNAME);	

		int docExpiry = 0;
		boolean randomExpiry = true;		// You can set this to true or false
		JsonDocument jsonDocument = null;
		int alreadyExistCount = 0;
		int otherExceptionCount = 0;
		
		for (int i = 0; i < NUMDOCUMENTS; i++) {
	
			// create a document
			documentKey = DOCUMENTNAMEPREFIX + i;
			jsonDocumentString = "{  \"somedata\" : \"This is some document data\", \"name\" : \"testDocument\", \"serialNumber\" : " + i + " }";
			jsonObject = JsonObject.fromJson(jsonDocumentString);

			if (randomExpiry) {
				docExpiry = (int) (Math.random() * HIGHEXPIRATION);
			}
			else {
				docExpiry = 0;
			}
			
			System.out.println("docExpiry is " + docExpiry);
			
			try {
				jsonDocument = JsonDocument.create(documentKey, docExpiry, jsonObject);
			} catch (DocumentAlreadyExistsException daee) {
				alreadyExistCount++;
			} catch (Exception e) {
				otherExceptionCount++;
			}
			
			// insert the document
			bucket.insert(jsonDocument);			

			
		} // for each document
		
		System.out.println("Done.  Already exist: " + alreadyExistCount + " otherExceptionCount: " + otherExceptionCount);
	}

}
