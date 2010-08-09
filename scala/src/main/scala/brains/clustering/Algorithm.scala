package brains.clustering

trait Algorithm[T <: brains.DataPoint[_]] {
  type Cluster = Seq[T]
  type Clusters = Seq[Cluster]

  def cluster(k:Int, points:Cluster):Clusters
}
