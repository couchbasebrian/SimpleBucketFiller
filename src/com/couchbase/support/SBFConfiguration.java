package com.couchbase.support;

import com.couchbase.client.java.PersistTo;
import com.couchbase.client.java.ReplicateTo;

// Brian Williams
// July 7, 2016

public class SBFConfiguration {

	// constructor
	SBFConfiguration() {
		// do something
	}

	// You may set this to any value you like
	public String documentNamePrefix = "sbfDocument";

	// Do you want to upsert or insert ?
	// If upsertMode is true it will upsert()
	// otherwise it will insert()
	public boolean upsertMode = true;

	// Which durability options do you want?
	public PersistTo persistTo     = PersistTo.NONE;
	public ReplicateTo replicateTo = ReplicateTo.NONE;

	// Please override these with the values from your cluster and bucket
	public String HOSTNAME           = "localhost";
	public String BUCKETNAME         = "default";

	// Number of documents to try to insert
	public int    NUMDOCUMENTS       = 20;		

	// Should I start all over again once I reach NUMDOCUMENTS ?
	// If true, it will.  Otherwise it will not.
	public boolean continuousMode = false;

	// You can set this to true if you wish to have randomly set document
	// expirations, or false if you wish documents to have no expiration
	public boolean randomExpiry = false;		

	// Maximum expiration time from now, in seconds
	// Only used if randomExpiry is true
	// 1000000 seconds is about 12 days
	public int    highExpiration     = 60 * 60 * 24 * 2;	

	// You can set this to true for random doc sizes or false for more of a static doc
	public boolean randomSizes  = true;

	// If randomSizes is true, will create documents that randomly have
	// a size in this range, between minDocSize and maxDocSize, which are measured in bytes.
	// Large documents - 800kb to 1000kb
	public int	minDocSize	=  800000;
	public int 	maxDocSize	= 1000000;

	// These two options controls extra output logged to the console
	public boolean showYourWork = false;
	public boolean printEachDocument = false;

	// TODO
	// Support for connection timeout options such as
	// .setConnectTimeout()
	// .setReadTimeout()
	// .setOpTimeout
	// .setSoTimeout()
	// .setObsTimeout()

	// TODO
	// Add the ability to read itself from a properties file

	// TODO
	// Add a static class method with the ability to read a folder full of properties
	// files and return a list of these instantiated objects, with one object populated
	// from each properties file

	// Optional
	// For getting extra information from the Cluster / ClusterInfo object
	public String username = "Administrator";
	public String password = "";

} // class SBFConfiguration

// EOF
