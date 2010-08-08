package brains.clustering

import scala.annotation.tailrec

object SingleLink {

  @tailrec 
  private 
  def buildClusters(distance:Double, 
                    points:Seq[DataPoint], 
                    clusters:Seq[Seq[DataPoint]]):Seq[Seq[DataPoint]] = {
      if (points.isEmpty) clusters
      else {
        val p :: others = points.toList
        val (close, far) = others.partition(p.distance(_) < distance) // TODO: memoize
        buildClusters(distance, far, clusters :+ (close :+ p))
      }
  }

  private
  def cluster(k:Int, points:Seq[DataPoint]):Seq[Seq[DataPoint]] = {
    @tailrec
    def doCluster(clusters:Seq[Seq[DataPoint]], distance:Double):Seq[Seq[DataPoint]] = {
      val newClusters = buildClusters(distance, points, Seq())
      if (newClusters.size == k) newClusters
      else doCluster(newClusters, distance + 1)
    }

    val clusters = points map { Seq(_) }
    doCluster(clusters, 0)
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

