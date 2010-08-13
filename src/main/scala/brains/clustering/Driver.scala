package brains.clustering

import brains.NumericDataPoint

class Driver[M <: Method](implicit man:Manifest[M]) {

  def main(args:Array[String]) = {
    val numClusters = args.head.toInt
    val data = NumericDataPoint.readFile(args(1)).toSeq
    val method = man.erasure.newInstance.asInstanceOf[M]
    val clusters = method.cluster(numClusters, data).sortBy(_.size)
    for (c <- clusters) {
      println(c.map(_.label).sorted.mkString(" "))
    }
  }

}
