package com.couchbase.support;

// Brian Williams
// March 22, 2016
// Updated April 18, 2016
// You can use this program to raise the resident ratio of a bucket
// If you can determine the keys programmatically

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.document.JsonDocument;

// For Couchbase Java Client 2.1.3
// Also works with version 2.2.5

public class SimpleDocumentGetter {

	public static void main(String[] args) {

		// Please replace these with the values from your cluster and bucket
		String HOSTNAME           = "localhost";
		String BUCKETNAME         = "BUCKETNAME";

		// Number of documents to try to get
		int    NUMDOCUMENTS       = 1000;		

		// You may set this to any value you like
		String DOCUMENTNAMEPREFIX = "sbfDocument";

		String documentKey        = "";

		JsonDocument jsonDocument = null;

		int otherExceptionCount = 0;
		int successfulGet    = 0;
		int nullDocumentCount = 0;		

		// Connect to the cluster
		Cluster cluster = CouchbaseCluster.create(HOSTNAME);

		boolean getClusterInfo = true;

		if (getClusterInfo) {
			try {
				// The Admin ( UI login ) credentials go here
				ClusterInfo ci = getClusterManagerInfo(cluster, "Administrator", "password");

				System.out.println(ci.raw());
				System.out.println("Min version is: " + ci.getMinVersion());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Bucket bucket = cluster.openBucket(BUCKETNAME);	

		boolean continuousMode = true;

		while (continuousMode) {

			long startTime = System.currentTimeMillis();

			for (int i = 0; i < NUMDOCUMENTS; i++) {

				// specify a document to get
				documentKey = DOCUMENTNAMEPREFIX + i;

				try {

					// try to get the document
					jsonDocument = bucket.get(documentKey);			

					if (jsonDocument == null) {
						nullDocumentCount++;				
					}
					else {
						successfulGet++;
					}

				}  catch (Exception e) {
					otherExceptionCount++;
				}			

			} // for each document

			System.out.println();

			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;

			long docsPerSecond = ((NUMDOCUMENTS * 1000)/ elapsedTime);

			System.out.println("Done.  Successfully gotten:         " + successfulGet + " documents out of " + NUMDOCUMENTS + " tried.");
			System.out.println("Null document count:                " + nullDocumentCount);
			System.out.println("Number of other exceptions:         " + otherExceptionCount);
			System.out.println("Total run time was:                 " + elapsedTime + " millseconds");
			System.out.println("Performance was about:              " + docsPerSecond + " docs per second");

		} // continuous mode

	}

	// Added April 18, 2016
	public static ClusterInfo getClusterManagerInfo(Cluster c, String adminUser, String adminPassword) {

		ClusterManager cm = c.clusterManager(adminUser, adminPassword);

		ClusterInfo ci = cm.info();		

		return ci;
	}


}

// EOF