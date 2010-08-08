package brains.clustering

import brains.clustering.DataPoint.{Cluster,Clusters}

trait Driver {

  def cluster(k:Int, points:Cluster):Clusters

  def main(args:Array[String]) = {
    val numClusters = args.head.toInt
    val data = DataPoint.readFile(args(1))
    val clusters = cluster(numClusters, data).sortBy(_.size)
    for (c <- clusters) {
      val labels = c.map(_.label)
      println(labels.mkString(", "))
    }
  }

}
