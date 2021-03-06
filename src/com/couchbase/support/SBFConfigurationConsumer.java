package com.couchbase.support;

import java.util.ArrayList;
import java.util.List;

import com.couchbase.client.deps.io.netty.handler.timeout.TimeoutException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.error.DurabilityException;
import com.couchbase.client.java.util.features.Version;

//  Brian Williams
//  Created: July 7-8, 2016
//  Last Updated: July 12, 2016

public class SBFConfigurationConsumer {

	public SBFConfigurationConsumer() {
		// TODO
	}

	public static void main(String[] args) {
		System.out.println("-------- Welcome to SBFConfigurationConsumer --------");
		SBFConfigurationConsumer cc = new SBFConfigurationConsumer();
		cc.go();
		System.out.println("-------- Goodbye --------");

	}

	public void go() {

		SBFTestResults testResult = null;
		int configurationCounter  = 0;

		ArrayList<SBFConfiguration> configurationList = SBFConfigurationFactory.getConfigurationList();

		int numConfigs = configurationList.size();

		SBFTestResults[] allResults = new SBFTestResults[numConfigs];

		for (SBFConfiguration eachTestConfig : configurationList) {
			System.out.println("Working on configuration: " + configurationCounter);
			testResult = processConfiguration(eachTestConfig);
			testResult.print();
			allResults[configurationCounter] = testResult;
			System.out.println("-------- Done with #" + configurationCounter + " out of " + numConfigs + " --------");

			// Don't sleep on the final one
			if (configurationCounter < (numConfigs-1)) {
				try {
					System.out.println("-------- Sleeping... --------");
					Thread.sleep(5000); // pause 5 seconds between tests, wait for clean sdk shutdown
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			configurationCounter++;
		}

		System.out.println("-------- All done with configs --------");

		SBFTestResults.printList(allResults);

	}

	// Given one configuration, run it, and then return the results
	private SBFTestResults processConfiguration(SBFConfiguration config) {

		SBFTestResults results = new SBFTestResults(config);

		int		documentSizeRange = config.maxDocSize - config.minDocSize;

		StringBuffer sb           = new StringBuffer();
		String jsonDocumentString = "";
		String documentKey        = "";
		String randomLetters      = "";
		JsonObject   jsonObject   = null;
		JsonDocument jsonDocument = null;
		int sizeOfThisDocument    = 0;
		int docExpiry             = 0;
		int docRandomSize         = 0;
		char randomChar			  = 0;

		// Connect to the cluster
		Cluster cluster = CouchbaseCluster.create(config.HOSTNAME);
		Bucket  bucket  = cluster.openBucket(config.BUCKETNAME);	

		// TODO catch any exceptions that occur and set a flag on the Results object
		// if there is any problem connecting to the cluster or the bucket.

		System.out.println("I have connected to the cluster and bucket");

		// See if we can get the cluster nodes version list
		List<Version> clusterVersionList = null;
		if ((config.username.length() != 0) && (config.password.length() != 0)) {
			// We have been given both a user name and a password
			try {
				clusterVersionList = cluster.clusterManager(config.username, config.password).info().getAllVersions();

				// Add this information into the test results
				if (clusterVersionList != null) {
					for (Version v : clusterVersionList) {
						results.addVersion(v);
					}
				}
			} catch (Exception e) {
				System.out.println("Could not get additional cluster info using the supplied username and password");
			}
		}

		System.out.println("I will iterate over " + config.NUMDOCUMENTS + " documents");

		long startTime = System.currentTimeMillis();

		long t1 = 0, t2 = 0;
		long latency = 0;
		long cumulativeTime = 0;

		for (int i = 0; i < config.NUMDOCUMENTS; i++) {

			// create a document
			documentKey = config.documentNamePrefix + i;

			if (config.showYourWork) {
				System.out.println("The current document key is " + documentKey);
			}

			// Determine docExpiry based on the options
			if (config.randomExpiry) {
				docExpiry = (int) (Math.random() * config.highExpiration);
			}
			else {
				docExpiry = 0;
			}

			// Reset the string buffer
			sb.setLength(0);

			if (config.randomSizes) {
				docRandomSize = ((int) (Math.random() * documentSizeRange)) + config.minDocSize;

				for (int x = 0; x < docRandomSize; x++) {
					randomChar = (char) (((int) (Math.random() * (90 - 65)) + 65));	// a random ASCII char between 65 and 90
					sb.append(randomChar);
				}

				randomLetters = sb.toString();

				jsonDocumentString = "{  \"randomData\" : \"" + randomLetters + "\", \"name\" : \"" 
						+ documentKey +  "\", \"serialNumber\" : " + i; 
			} else {
				jsonDocumentString = "{  \"body\" : \"This is some document data\", \"name\" : \"" 
						+ documentKey + "\", \"serialNumber\" : " + i;
			}

			// Add expiry into the JSON, for now - https://issues.couchbase.com/browse/MB-15916
			if (config.randomExpiry) {
				jsonDocumentString = jsonDocumentString + " , \"expiry\" : " + docExpiry;
			}

			// Close it off
			jsonDocumentString = jsonDocumentString + " }";

			sizeOfThisDocument = jsonDocumentString.length();

			if (config.printEachDocument) { System.out.println(jsonDocumentString + " Length: " + sizeOfThisDocument); }

			// Create the JSON Object from the generated json document string
			jsonObject = JsonObject.fromJson(jsonDocumentString);

			if (config.showYourWork) {
				System.out.println("Working on document #" + i + " of " 
						+ config.NUMDOCUMENTS + ". Using expiration of " 
						+ docExpiry + " seconds");
			}

			try {
				jsonDocument = JsonDocument.create(documentKey, docExpiry, jsonObject);

				//
				// Here is the actual operation
				// 

				t1 = System.currentTimeMillis();

				if (config.upsertMode) {
					bucket.upsert(jsonDocument, config.persistTo, config.replicateTo);			
				}
				else {
					// insert the document
					bucket.insert(jsonDocument, config.persistTo, config.replicateTo);			
				}

				// If we got here, insert was successful.  Do some bookkeeping.
				t2 = System.currentTimeMillis();

				latency = t2 - t1;
				cumulativeTime += latency;

				System.out.println("The latency for this successful op was " + latency);

				results.successfulInsert++;
				results.cumulativeDocSize = results.cumulativeDocSize + sizeOfThisDocument;
				if (sizeOfThisDocument < results.minGeneratedSize) { results.minGeneratedSize = sizeOfThisDocument; }
				if (sizeOfThisDocument > results.maxGeneratedSize) { results.maxGeneratedSize = sizeOfThisDocument; }

				results.averageLatency = cumulativeTime / results.successfulInsert;

			} catch (DocumentAlreadyExistsException daee) {
				results.alreadyExistCount++;
			} catch (DurabilityException dure) {
				results.durabilityException++;
			} catch (TimeoutException tme) {
				results.timeoutExceptions++;
			} catch (RuntimeException rte) {
				results.runtimeExceptions++;
			} catch (Exception e) {
				System.out.println(e);
				results.otherExceptionCount++;
			}			

		} // for each document

		long endTime = System.currentTimeMillis();

		// Store some final things in the results
		results.elapsedTime = endTime - startTime;
		results.docsPerSecond = ((config.NUMDOCUMENTS * 1000)/ results.elapsedTime);
		results.sdkVersionString= bucket.environment().packageNameAndVersion();

		// Clean Up really well
		bucket.close();
		bucket = null;
		cluster.disconnect();
		cluster = null;

		return results;

	} // processConfiguration() method

} // SBFConfigurationConsumer class

// EOF