# SimpleBucketFiller

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
