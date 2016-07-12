package com.couchbase.support;

import java.util.ArrayList;

import com.couchbase.client.java.PersistTo;
import com.couchbase.client.java.ReplicateTo;

public class SBFConfigurationFactory {

	static ArrayList<SBFConfiguration> getConfigurationList() {

		String cluster1Hostname = "172.23.105.232";
		String cluster2Hostname = "172.23.105.230";

		ArrayList<SBFConfiguration> configurationList = new ArrayList<SBFConfiguration>();

		SBFConfiguration c;

		// First Cluster - 4.1.0

		// persist to none, replicate to none - the default
		c = new SBFConfiguration();
		c.HOSTNAME = cluster1Hostname;
		c.documentNamePrefix = "test1-";
		configurationList.add(c);

		// persist to ONE, replicate to none
		c = new SBFConfiguration();
		c.documentNamePrefix = "test2-";
		c.HOSTNAME  = cluster1Hostname;
		c.persistTo = PersistTo.ONE;
		configurationList.add(c);

		// persist to none, replicate to ONE
		c = new SBFConfiguration();
		c.documentNamePrefix = "test3-";
		c.HOSTNAME    = cluster1Hostname;
		c.replicateTo = ReplicateTo.ONE;
		configurationList.add(c);

		// persist to ONE, replicate to ONE
		c = new SBFConfiguration();
		c.documentNamePrefix = "test4-";
		c.HOSTNAME    = cluster1Hostname;
		c.persistTo   = PersistTo.ONE;
		c.replicateTo = ReplicateTo.ONE;
		configurationList.add(c);

		// Second Cluster - 4.1.1

		// persist to none, replicate to none - the default
		c = new SBFConfiguration();
		c.documentNamePrefix = "test5-";
		c.HOSTNAME = cluster2Hostname;
		c.documentNamePrefix = "test1-";
		configurationList.add(c);

		// persist to ONE, replicate to none
		c = new SBFConfiguration();
		c.documentNamePrefix = "test6-";
		c.HOSTNAME  = cluster2Hostname;
		c.persistTo = PersistTo.ONE;
		configurationList.add(c);

		// persist to none, replicate to ONE
		c = new SBFConfiguration();
		c.documentNamePrefix = "test7-";
		c.HOSTNAME    = cluster2Hostname;
		c.replicateTo = ReplicateTo.ONE;
		configurationList.add(c);

		// persist to ONE, replicate to ONE
		c = new SBFConfiguration();
		c.documentNamePrefix = "test8-";
		c.HOSTNAME    = cluster2Hostname;
		c.persistTo   = PersistTo.ONE;
		c.replicateTo = ReplicateTo.ONE;
		configurationList.add(c);

		return configurationList;

	}

}
