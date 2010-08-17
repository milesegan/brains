package brains.clustering

import brains.NumericDataPoint
import scala.annotation.tailrec

/**
 * Clusters numeric datasets via the k-means method.
 * @see <a href="http://en.wikipedia.org/wiki/K-means_clustering">K-means clustering</a>.
 */
class KMeans extends Method {

  type Doubles = Seq[Double]
  type Centroids = Seq[Doubles]

  def cluster(k: Int, points: Cluster): Clusters = {

    @tailrec
    def doCluster(centroids: Centroids, clusters: Map[Int,Cluster]): Clusters = {
      val newClusters = points.groupBy { closestCentroid(centroids, _) }
      val newCentroids = for (i <- 0 until k) yield centroid(newClusters(i))
      if (centroids == newCentroids)
        clusters.values.toSeq
      else
        doCluster(newCentroids, newClusters)
    }

    val centroids = pickInitialCentroids(k, points)
    doCluster(centroids, Map())
  }

  private
  def closestCentroid(centroids: Centroids, p: NumericDataPoint): Int = {
    val distances = centroids.map{ p.distance }.zipWithIndex.sorted
    distances.head._2
  }

  private
  def centroid(elements: Cluster): Doubles = {
    val values = elements.map(_.values)
    val sums = for (i <- values.head.indices) yield values.map(_(i)).sum
    sums.map(_ / elements.size)
  }

  private
  def pickInitialCentroids(k: Int, points: Cluster): Centroids = {
    util.Random.shuffle(points.toSeq).take(k).map(_.values)
  }

}

object KMeans extends Driver[KMeans]
