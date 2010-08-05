package clustering

import collection.mutable.{ HashSet => MSet, Buffer => MBuf }

class KMeans(val points:Seq[DataPoint]) {

  def cluster(k:Int):Seq[Seq[DataPoint]] = {
    var centroids:Seq[Seq[Double]] = pickInitialCentroids(k, points)

    while (true) {
      val clusters = for (i <- 0 until k) yield MBuf[DataPoint]()
      for (p <- points) {
        val i = closestCentroid(centroids, p)
        clusters(i) += p
      }

      val newCentroids = for (c <- clusters) yield centroid(c)
      if (centroids equals newCentroids) {
        return clusters
      }
      centroids = newCentroids
    }
    Seq()
  }

  private
  def closestCentroid(centroids:Seq[Seq[Double]], p:DataPoint):Int = {
    val distances = centroids.map(distance(p.values, _)).zipWithIndex.sorted
    distances.head._2
  }

  private
  def centroid(elements:Seq[DataPoint]):Seq[Double] = {
    if (elements.isEmpty) return Seq()
    
    val points = elements.map(_.values)
    val sums = for (i <- 0 until points.head.size) yield points.map(_(i)).sum
    sums.map(_ / elements.size)
  }

  private
  def distance(a:Seq[Double], b:Seq[Double]):Double = {
    val squares = (a zip b).map{ case (x,y) => math.pow((x - y), 2) }
    math.sqrt(squares.sum)
  }

  private
  def pickInitialCentroids(k:Int, points:Seq[DataPoint]):Seq[Seq[Double]] = {
    // pick existing data points as starting centroids
    util.Random.shuffle(points) take(k) map(_.values)
  }
}

object KMeans {
  def openFile(path:String) = new io.BufferedSource(new java.io.FileInputStream(path))

  def main(args:Array[String]) = {
    val numClusters = args.head.toInt
    val src = openFile(args(1)).getLines.drop(1)
    val dataLines = src.map(_.split(""",\s+""").toList)
    val data = dataLines.map(i => DataPoint(i.tail.map(_.toDouble), i.head)).toSeq
    val km = new KMeans(data)
    val clusters = km.cluster(numClusters).sortBy(_.size)
    for (c <- clusters) {
      val labels = c.map(_.label)
      println(labels.mkString(", "))
    }
  }
}
