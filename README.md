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

# SimpleDocumentGetter

Utility to get each document in the bucket, where the keys can be programmatically determined.  Can raise the resident ratio of a bucket.

Sample output

    Done.  Successfully gotten:         166 documents out of 1000 tried.
    Null document count:                834
    Number of other exceptions:         0
    Total run time was:                 626 millseconds
    Performance was about:              1597 docs per second
