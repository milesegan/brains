package brains.clustering

import scala.annotation.tailrec

object SingleLink extends Algorithm[brains.NumericDataPoint] with Driver {
  
  @tailrec 
  private 
  def buildClusters(distance:Double, points:Cluster, clusters:Clusters):Clusters = {
      if (points.isEmpty) clusters
      else {
        val p :: others = points.toList
        val (close, far) = others.partition(p.distance(_) < distance)
        buildClusters(distance, far, clusters :+ (close :+ p))
      }
  }

  def cluster(k:Int, points:Cluster):Clusters = {
    @tailrec
    def doCluster(clusters:Clusters, distance:Double):Clusters = {
      val newClusters = buildClusters(distance, points, Seq())
      if (newClusters.size <= k) newClusters
      else doCluster(newClusters, distance + 1)
    }

    val clusters = points map { Seq(_) }
    doCluster(clusters, 0)
  }

}

