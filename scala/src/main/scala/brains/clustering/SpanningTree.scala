package brains.clustering

import scala.annotation.tailrec

object SpanningTree extends Algorithm[brains.NumericDataPoint] with Driver {
  
  @tailrec 
  private 
  def buildClusters(d:Double, points:Cluster, thisCluster:Cluster, clusters:Clusters):Clusters = {
      if (points.isEmpty) clusters
      else {
        val current = thisCluster.head
        val sorted = points.sortBy { current distance _ }
        val (closest, rest) = (sorted.head, sorted.tail)
        if (current.distance(closest) < d)
          buildClusters(d, rest, thisCluster :+ closest, clusters)
        else
          buildClusters(d, points.tail, Seq(points.head), clusters :+ thisCluster)
      }
  }

  def cluster(k:Int, points:Cluster):Clusters = {
    @tailrec
    def doCluster(clusters:Clusters, distance:Double):Clusters = {
      val newClusters = buildClusters(distance, points.tail, Seq(points.head), Seq())
      if (newClusters.size == k) newClusters
      else doCluster(newClusters, distance + 1)
    }

    val clusters = points map { Seq(_) }
    doCluster(clusters, 0)
  }

}

