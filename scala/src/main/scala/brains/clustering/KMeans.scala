package brains.clustering

import brains.NumericDataPoint
import scala.annotation.tailrec

object KMeans extends Algorithm[NumericDataPoint] with Driver {

  type Doubles = Seq[Double]

  def cluster(k:Int, points:Cluster):Clusters = {

    @tailrec
    def doCluster(centroids:Cluster, clusters:Map[Int,Cluster]):Clusters = {
      val newClusters = points.groupBy { closestCentroid(centroids, _) }
      val newCentroids = for (i <- 0 until k) yield centroid(newClusters(i))
      if (centroids.map(_.values) equals newCentroids.map(_.values))
        clusters.values.toSeq
      else
        doCluster(newCentroids, newClusters)
    }

    val centroids = pickInitialCentroids(k, points)
    doCluster(centroids, Map())
  }

  private
  def closestCentroid(centroids:Cluster, p:NumericDataPoint):Int = {
    val distances = centroids.map{ p.distance }.zipWithIndex.sorted
    distances.head._2
  }

  private
  def centroid(elements:Cluster):NumericDataPoint = {
    val values = elements.map(_.values)
    val sums = for (i <- values.head.keySet) yield (i, values.map(_.getOrElse(i, 0d)).sum)
    val newVals = Map.empty ++ sums.map { case(k,v) => (k, v / elements.size) }
    new NumericDataPoint(newVals, "centroid")
  }

  private
  def pickInitialCentroids(k:Int, points:Cluster):Cluster = {
    util.Random.shuffle(points.toSeq).take(k)
  }

}
