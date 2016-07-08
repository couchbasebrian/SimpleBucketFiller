package com.couchbase.support;

//Brian Williams
//July 7, 2016

public class SBFTestResults {

	SBFTestResults(SBFConfiguration c) {
		configurationUsed = c;
	}

	private SBFConfiguration configurationUsed = null;
	
	public int alreadyExistCount   = 0;
	public int durabilityException = 0;
	public int otherExceptionCount = 0;
	public int successfulInsert    = 0;
	public long elapsedTime        = 0;
	public long docsPerSecond      = 0;
	public int minGeneratedSize    = 100000000;   // an unlikely small size
	public int maxGeneratedSize    = 0;           // an unlikely large size
	public int cumulativeDocSize   = 0;           // sum total of all json doc strings

	public String versionString = "";

	public int timeoutExceptions = 0;

	public void print() {

		System.out.println("Hostname:                             " + configurationUsed.HOSTNAME);		
		System.out.println("Persist To:                           " + configurationUsed.persistTo);		
		System.out.println("Replicate To:                         " + configurationUsed.replicateTo);		
		System.out.println("SDK version is:                       " + versionString);
		System.out.println("Successfully inserted:                " + successfulInsert + " documents.");
		System.out.println("# of documents that already existed:  " + alreadyExistCount);
		System.out.println("Number of durability exceptions:      " + durabilityException);
		System.out.println("Number of timeout exceptions:         " + timeoutExceptions);
		System.out.println("Number of other exceptions:           " + otherExceptionCount);
		System.out.println("Total run time was:                   " + elapsedTime + " millseconds");
		System.out.println("Performance was about:                " + docsPerSecond + " docs per second");
		System.out.println("Total successful doc size was:        " + cumulativeDocSize + " bytes");
		System.out.println("Generated doc size range was between  " + minGeneratedSize + " and " + maxGeneratedSize + " bytes");

	} // print() method

} // SBFTestResults class

// EOF