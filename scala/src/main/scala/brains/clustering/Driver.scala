package brains.clustering

import brains.NumericDataPoint

trait Driver {

  def cluster(k:Int, points:Algorithm[NumericDataPoint]#Cluster):Algorithm[NumericDataPoint]#Clusters

  def main(args:Array[String]) = {
    val numClusters = args.head.toInt
    val data = NumericDataPoint.readFile(args(1)).toSeq
    val clusters = cluster(numClusters, data).sortBy(_.size)
    for (c <- clusters) {
      println(c.map(_.label).sorted.mkString(" "))
    }
  }

}
