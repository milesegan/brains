package brains.clustering

import scala.annotation.tailrec

/**
 * Clusters numeric datasets via the k-means method.
 * @see <a href="http://en.wikipedia.org/wiki/K-means_clustering">K-means clustering</a>.
 */
class KMeans extends Method {

  type Centroids = Seq[Point]

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
  def closestCentroid(centroids: Centroids, p: Point): Int = {
    val distances = centroids.map{ distance(p, _) }.zipWithIndex.sorted
    distances.head._2
  }

  private
  def centroid(elements: Cluster): Point = {
    val sums = for ((k,v) <- elements.head) yield {
      (k, elements.map(_(k)).sum / elements.size)
    }
    Map.empty ++ sums
  }

  private
  def pickInitialCentroids(k: Int, points: Cluster): Centroids = {
    util.Random.shuffle(points.toSeq).take(k)
  }

}

object KMeans extends Driver[KMeans]
