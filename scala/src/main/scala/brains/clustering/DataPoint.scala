package brains.clustering

case class DataPoint(val values:Seq[Double], val label:String)

object DataPoint {
  def readFile(path:String) = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val lines = src.getLines.drop(1)
    val dataLines = lines map { _.split(""",\s+""").toList }
    dataLines.map(i => DataPoint(i.tail map { _.toDouble }, i.head)).toSeq
  }
}

