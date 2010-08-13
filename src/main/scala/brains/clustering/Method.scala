package brains.clustering

trait Method {
  type Cluster = Seq[brains.NumericDataPoint]
  type Clusters = Seq[Cluster]

  def cluster(k:Int, points:Cluster):Clusters
}
