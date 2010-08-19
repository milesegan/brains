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

  /**
   * Computes a 2-d array of distances between each pair of
   * points.
   */
  def distanceMap(points: Cluster): Array[Array[Double]] = {
    val n = points.size
    val d = Array.ofDim[Double](n, n)
    for (i <- 0 until n; j <- i + 1 until n) {
      val thisD = distance(points(i), points(j))
      d(i)(j) = thisD
      d(j)(i) = thisD
    }
    d
  }

  /**
   * Computes the euclidean distance between two points.
   */
  def distance(a: Point, b: Point): Double = {
    val squares = for ((ak,av) <- a; bv = b(ak)) yield math.pow(av - bv, 2)
    math.sqrt(squares.sum)
  }
}
