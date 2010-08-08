package brains.clustering

trait Algorithm {
  type Cluster = Seq[DataPoint]
  type Clusters = Seq[Cluster]

  def cluster(k:Int, points:Cluster):Clusters
}
