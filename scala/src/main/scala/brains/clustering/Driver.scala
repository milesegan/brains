package brains.clustering

trait Driver {

  def cluster(k:Int, points:Algorithm#Cluster):Algorithm#Clusters

  def main(args:Array[String]) = {
    val numClusters = args.head.toInt
    val data = DataPoint.readFile(args(1))
    val clusters = cluster(numClusters, data).sortBy(_.size)
    for (c <- clusters) {
      println(c.map(_.label).sorted.mkString(" "))
    }
  }

}
