package com.couchbase.support;

import java.util.HashSet;
import java.util.Set;

import com.couchbase.client.java.util.features.Version;

//  Brian Williams
//  July 7, 2016

public class SBFTestResults {

	SBFTestResults(SBFConfiguration c) {
		configurationUsed = c;
	}

	private SBFConfiguration configurationUsed = null;
	private Set<Version> couchbaseVersions = null;
	private int numberOfNodes = 0;

	public String sdkVersionString = "";

	public int alreadyExistCount   = 0;
	public int durabilityException = 0;
	public int otherExceptionCount = 0;
	public int successfulInsert    = 0;
	public long elapsedTime        = 0;
	public long docsPerSecond      = 0;
	public int minGeneratedSize    = 100000000;   // an unlikely small size
	public int maxGeneratedSize    = 0;           // an unlikely large size
	public int cumulativeDocSize   = 0;           // sum total of all json doc strings
	public int timeoutExceptions   = 0;
	public int runtimeExceptions   = 0;
	public long averageLatency     = 0;

	// This typically gets called once for each Version found, 
	// which is usually one per node.  So we can count the number
	// nodes and store this in the results.
	public void addVersion(Version v) {
		if (couchbaseVersions == null){
			couchbaseVersions = new HashSet<Version>();
		}
		couchbaseVersions.add(v);
		numberOfNodes++;
	}
	
	public String getVersionList() {
		String rval = "";
		if (couchbaseVersions != null) {
			for (Version v : couchbaseVersions) {
				rval = rval + v;
			}
		}
		else {
			rval = "N/A";
		}
		return rval;
	}

	public void print() {
		System.out.println("SDK version is:                       " + sdkVersionString);
		System.out.println("Hostname:                             " + configurationUsed.HOSTNAME);		
		System.out.println("Node version(s):                      " + getVersionList());		
		System.out.println("Number of nodes:                      " + numberOfNodes);		
		System.out.println("Persist To:                           " + configurationUsed.persistTo);		
		System.out.println("Replicate To:                         " + configurationUsed.replicateTo);		
		System.out.println("Successfully inserted:                " + successfulInsert + " documents out of " + configurationUsed.NUMDOCUMENTS);
		System.out.println("Upsert Mode:                          " + configurationUsed.upsertMode);
		System.out.println("# of documents that already existed:  " + alreadyExistCount);
		System.out.println("Number of durability exceptions:      " + durabilityException);
		System.out.println("Number of timeout exceptions:         " + timeoutExceptions);
		System.out.println("Number of runtime exceptions:         " + runtimeExceptions);
		System.out.println("Number of other exceptions:           " + otherExceptionCount);
		System.out.println("Total run time was:                   " + elapsedTime + " milliseconds");
		System.out.println("Performance was approximately:        " + docsPerSecond + " docs per second");
		System.out.println("Average latency for successful ops:   " + averageLatency + " milliseconds");
		System.out.println("Total size of inserted docs:          " + cumulativeDocSize + " bytes");
		System.out.println("Generated doc size range was between  " + minGeneratedSize + " and " + maxGeneratedSize + " bytes");
	} // print() method

} // SBFTestResults class

// EOF