package brains.clustering

import scala.annotation.tailrec
import brains.clustering.DataPoint.{Cluster,Clusters}

object KMeans extends Driver {

  type Doubles = Seq[Double]

  def cluster(k:Int, points:Cluster):Clusters = {

    @tailrec
    def doCluster(centroids:Seq[Doubles], clusters:Map[Int,Cluster]):Clusters = {
      val newClusters = points.groupBy { closestCentroid(centroids, _) }
      val newCentroids = for (i <- 0 until k) yield centroid(newClusters(i))
      if (centroids equals newCentroids)
        clusters.values.toSeq
      else
        doCluster(newCentroids, newClusters)
    }

    val centroids:Seq[Doubles] = pickInitialCentroids(k, points)
    doCluster(centroids, Map())
  }

  private
  def closestCentroid(centroids:Seq[Doubles], p:DataPoint):Int = {
    val distances = centroids.map{ p.distance }.zipWithIndex.sorted
    distances.head._2
  }

  private
  def centroid(elements:Cluster):Doubles = {
    if (elements.isEmpty) return Seq()
    
    val points = elements map { _.values }
    val sums = for (i <- 0 until points.head.size) yield points.map(_(i)).sum
    sums map { _ / elements.size }
  }

  private
  def pickInitialCentroids(k:Int, points:Cluster):Seq[Doubles] = {
    util.Random.shuffle(points.toSeq).take(k) map { _.values }
  }

}
