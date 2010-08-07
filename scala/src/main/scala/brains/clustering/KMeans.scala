package brains.clustering

import scala.annotation.tailrec

object KMeans {

  def cluster(k:Int, points:Seq[DataPoint]):Seq[Seq[DataPoint]] = {

    @tailrec
    def doCluster(centroids:Seq[Seq[Double]], clusters:Map[Int,Seq[DataPoint]]):Seq[Seq[DataPoint]] = {
      val newClusters = points.groupBy { closestCentroid(centroids, _) }
      val newCentroids = for (i <- 0 until k) yield centroid(newClusters(i))
      if (centroids equals newCentroids)
        clusters.values.toSeq
      else
        doCluster(newCentroids, newClusters)
    }

    val centroids:Seq[Seq[Double]] = pickInitialCentroids(k, points)
    doCluster(centroids, Map())
  }

  private
  def closestCentroid(centroids:Seq[Seq[Double]], p:DataPoint):Int = {
    val distances = centroids.map{ p.distance }.zipWithIndex.sorted
    distances.head._2
  }

  private
  def centroid(elements:Seq[DataPoint]):Seq[Double] = {
    if (elements.isEmpty) return Seq()
    
    val points = elements map { _.values }
    val sums = for (i <- 0 until points.head.size) yield points.map(_(i)).sum
    sums map { _ / elements.size }
  }

  private
  def pickInitialCentroids(k:Int, points:Seq[DataPoint]):Seq[Seq[Double]] = {
    util.Random.shuffle(points) take(k) map(_.values)
  }

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
