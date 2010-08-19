package brains.clustering

import brains.Data

/**
 * Command line & test driver for clustering methods.
 */
class Driver[M <: Method](implicit man: Manifest[M]) {

  /**
   * Clusters the points in a datafile.
   *
   * @param args Should contain first an integer specifying the number of
   * target clusters and second a file containing the data points.
   *
   * For example:
   *
   * <code>
   * main("4", "data/iris.csv")
   * </code>
   */
  def main(args: Array[String]) = {
    val numClusters = args.head.toInt
    val data = Data.loadNumberData(args(1))
    val method = man.erasure.newInstance.asInstanceOf[M]
    val clusters = method.cluster(numClusters, data).sortBy(_.size)
    for (c <- clusters) {
      println(c.map(_('class)).mkString(" "))
      println()
    }
  }

}
