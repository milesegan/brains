package brains.clustering

case class DataPoint(val values:Seq[Double], val label:String)  {

  def distance(other:DataPoint):Double = {
    // memoize
    DataPoint.cache.getOrElseUpdate((this,other), distance(other.values))
  }

  def distance(other:Seq[Double]):Double = {
    val squares = (values zip other) map { case (x,y) => math.pow((x - y), 2) }
    math.sqrt(squares.sum)
  }
}

object DataPoint {

  private val cache = collection.mutable.Map.empty[(DataPoint,DataPoint), Double]

  type Cluster = Seq[DataPoint]
  type Clusters = Seq[Cluster]

  def readFile(path:String) = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val lines = src.getLines.drop(1)
    val dataLines = lines map { _.split(""",\s+""").toList }
    dataLines.map(i => DataPoint(i.tail map { _.toDouble }, i.head)).toSeq
  }
}

