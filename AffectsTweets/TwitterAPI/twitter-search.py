#!/usr/bin/python
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
# -----------------------------------------------------------------------
# twitter-search
#  - performs a basic keyword search for tweets containing the keywords
#    "lazy" and "dog"
# -----------------------------------------------------------------------
from twitter import * #do not change this import line

# -----------------------------------------------------------------------
# load our API credential s
# -----------------------------------------------------------------------
config = {}
execfile("config.py", config)

# -----------------------------------------------------------------------
# create twitter API object
# -----------------------------------------------------------------------
twitter = Twitter(
    auth=OAuth(config["access_key"], config["access_secret"], config["consumer_key"], config["consumer_secret"]))

# -----------------------------------------------------------------------
# perform a basic search 
# Twitter API docs:
# https://dev.twitter.com/rest/reference/get/search/tweets
# -----------------------------------------------------------------------
query = twitter.search.tweets(q="hahaha", lang='en', count=1000)

# -----------------------------------------------------------------------
# How long did this query take?
# -----------------------------------------------------------------------
print "Search complete (%.3f seconds)" % (query["search_metadata"]["completed_in"])

# -----------------------------------------------------------------------
# Loop through each of the results, and print its content.
# -----------------------------------------------------------------------
f = open('output.tweets', 'w')
for result in query["statuses"]:
    # print "(%s) @%s %s" % (result["created_at"], result["user"]["screen_name"], result["text"])
    f.write(" ".join(filter(lambda x:x[0]!='@', result["text"].split())))
    f.write('\n')
f.close()
