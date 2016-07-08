package com.couchbase.support;

import java.util.ArrayList;

import com.couchbase.client.java.PersistTo;
import com.couchbase.client.java.ReplicateTo;

public class SBFConfigurationFactory {

	static ArrayList<SBFConfiguration> getConfigurationList() {

		// 10.111.110.101 is a 4.1.0 cluster
		// 10.111.111.104 is a 4.1.1 cluster

		ArrayList<SBFConfiguration> configurationList = new ArrayList<SBFConfiguration>();

		SBFConfiguration c;

		// First Cluster - 4.1.0

		// persist to none, replicate to none - the default
		c = new SBFConfiguration();
		c.HOSTNAME = "10.111.110.101";
		c.documentNamePrefix = "test1-";
		configurationList.add(c);

		// persist to ONE, replicate to none
		c = new SBFConfiguration();
		c.documentNamePrefix = "test2-";
		c.HOSTNAME  = "10.111.110.101";
		c.persistTo = PersistTo.ONE;
		configurationList.add(c);

		// persist to none, replicate to ONE
		c = new SBFConfiguration();
		c.documentNamePrefix = "test3-";
		c.HOSTNAME    = "10.111.110.101";
		c.replicateTo = ReplicateTo.ONE;
		configurationList.add(c);

		// persist to ONE, replicate to ONE
		c = new SBFConfiguration();
		c.documentNamePrefix = "test4-";
		c.HOSTNAME    = "10.111.110.101";
		c.persistTo   = PersistTo.ONE;
		c.replicateTo = ReplicateTo.ONE;
		configurationList.add(c);

		// Second Cluster - 4.1.1

		// persist to none, replicate to none - the default
		c = new SBFConfiguration();
		c.documentNamePrefix = "test5-";
		c.HOSTNAME = "10.111.111.104";
		c.documentNamePrefix = "test1-";
		configurationList.add(c);

		// persist to ONE, replicate to none
		c = new SBFConfiguration();
		c.documentNamePrefix = "test6-";
		c.HOSTNAME  = "10.111.111.104";
		c.persistTo = PersistTo.ONE;
		configurationList.add(c);

		// persist to none, replicate to ONE
		c = new SBFConfiguration();
		c.documentNamePrefix = "test7-";
		c.HOSTNAME    = "10.111.111.104";
		c.replicateTo = ReplicateTo.ONE;
		configurationList.add(c);

		// persist to ONE, replicate to ONE
		c = new SBFConfiguration();
		c.documentNamePrefix = "test8-";
		c.HOSTNAME    = "10.111.111.104";
		c.persistTo   = PersistTo.ONE;
		c.replicateTo = ReplicateTo.ONE;
		configurationList.add(c);

		return configurationList;

	}

}
