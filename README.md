# SBF*

There are some new classes here that represent a new version of the original simple program.  I have taken the original SimpleBucketFiller and refactored it so that there is now a class to represent a test configuration, test results, a factory, and a configuration consumer.  The main, runnable class is SBFConfigurationConsumer.  When it starts up it gets a list of test configurations, SBFConfiguration objects, from the SBFConfigurationFactory.  Then it iterates over them, performs the test according to the specified options, and records the results in SBFTestResults.  There is also now support for durability options.

Sample output

    Hostname:                             10.111.110.101
    Persist To:                           NONE
    Replicate To:                         NONE
    SDK version is:                       couchbase-java-client/2.2.6 (git: 2.2.6, core: 1.2.7)
    Successfully inserted:                10 documents.
    # of documents that already existed:  0
    Number of durability exceptions:      0
    Number of timeout exceptions:         0
    Number of other exceptions:           0
    Total run time was:                   697 millseconds
    Performance was about:                14 docs per second
    Total successful doc size was:        9179977 bytes
    Generated doc size range was between  834757 and 985248 bytes

Note:  As a single-threaded, synchronous, application, one single instance of this program will not come close to reaching the maximum ops/sec capacity of most clusters.

# original SimpleBucketFiller

Utility for inserting documents into a bucket with random expiration times and random sizes.

Sample output

    Done.  Successfully inserted:         1000 documents.
    # of documents that already existed:  0
    Number of other exceptions:           0
    Total run time was:                   1312 millseconds
    Performance was about:                762 docs per second
    Total successful doc size was:        85125 bytes
    Generated doc size range was between  21 and 90 bytes

"expiry" property is now added into the document.  So if you use this utility to push code into Couchbase 4.x, you can use N1QL to then query for a histogram of the document expiration times.  For example, if you want to see document expirations broken down into 100 second buckets:

    select trunc(expiry / 100) AS expBucket, count(*) 
    AS bucketSize from `BUCKETNAME` 
    group by trunc(expiry/100) 
    order by trunc(expiry/100);

The results will look something like this

    "results": [
        {
            "bucketSize": 25,
            "expBucket": 33
        },
        {
            "bucketSize": 11,
            "expBucket": 34
        },
        {
            "bucketSize": 26,
            "expBucket": 35
        }
    ],


# SimpleDocumentGetter

Utility to get each document in the bucket, where the keys can be programmatically determined.  Can raise the resident ratio of a bucket.

Sample output

    Done.  Successfully gotten:         166 documents out of 1000 tried.
    Null document count:                834
    Number of other exceptions:         0
    Total run time was:                 626 millseconds
    Performance was about:              1597 docs per second
