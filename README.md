# brains
This is the home of my ongoing experiments in machine learning in
scala.

So far it encompasses the following:

## recommendations and similarity measures

## clustering methods
* k-means 
* spanning tree
* single-link

## classification methods
* naive bayes
* neural net
* decision tree

To run a quick test try this:

install sbt

at the root of this project run the following commands:

    sbt 'classify bayes poisonous data/mushroom.csv'
    sbt 'classify dtree poisonous data/mushroom.csv'

scaladoc for the project is available at http://burgerkone.com/brains/
