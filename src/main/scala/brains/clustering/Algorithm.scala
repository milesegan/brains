package brains.clustering

trait Algorithm {
  type Cluster = Seq[brains.NumericDataPoint]
  type Clusters = Seq[Cluster]

  def cluster(k:Int, points:Cluster):Clusters
}
