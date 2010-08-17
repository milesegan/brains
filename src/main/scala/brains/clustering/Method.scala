package brains.clustering

/**
 * Common interface provided by all clustering methods.
 */
trait Method {
  type Cluster = Seq[brains.data.NumericDataPoint]
  type Clusters = Seq[Cluster]

  /**
   * Divides the supplied points into clusters.
   *
   * @param k      The number of resulting clusters.
   * @param points The dataset of points to be clustered.
   */
  def cluster(k: Int, points: Cluster): Clusters
}
