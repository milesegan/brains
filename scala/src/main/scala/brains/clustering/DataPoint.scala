package brains.clustering

case class DataPoint(val values:Seq[Double], val label:String) {

  def distance(b:Seq[Double]):Double = {
    val squares = (values zip b) map { case (x,y) => math.pow((x - y), 2) }
    math.sqrt(squares.sum)
  }

  def distance(p:DataPoint):Double = distance(p.values)
}

object DataPoint {

  type Cluster = Seq[DataPoint]
  type Clusters = Seq[Cluster]

  def readFile(path:String) = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val lines = src.getLines.drop(1)
    val dataLines = lines map { _.split(""",\s+""").toList }
    dataLines.map(i => DataPoint(i.tail map { _.toDouble }, i.head)).toSeq
  }
}

