package brains.clustering

/**
 * Common interface provided by all clustering methods.
 */
trait Method {

  type Point = Map[Symbol,Double]
  type Cluster = Seq[Point]
  type Clusters = Seq[Cluster]

  /**
   * Divides the supplied points into clusters.
   *
   * @param k      The number of resulting clusters.
   * @param points The dataset of points to be clustered.
   */
  def cluster(k: Int, points: Cluster): Clusters

  def distance(a: Point, b: Point): Double = {
    val squares = for ((ak,av) <- a; bv = b(ak)) yield math.pow(av - bv, 2)
    math.sqrt(squares.sum)
  }
}
