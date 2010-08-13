package brains

import brains.util._

object DataFile {

  def load(path:String):(Seq[Symbol],Seq[Seq[String]]) = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val lines = for (i <- src.getLines if !i.matches("""^(#.*|\s*)$""")) yield {
      i.trim.split("""\s?,\s?""").toSeq
    }
    val fields = lines.next.map { Symbol(_) }
    (fields, lines.toSeq)
  }

}

case class StringDataPoint(values:Map[Symbol,String], label:String)

object StringDataPoint {

  def readFile(path:String):Seq[StringDataPoint] = {
    val (fields, lines) = DataFile.load(path)
    for (d <- lines) yield {
      val label = d.head
      val values = d.tail
      new StringDataPoint(Map.empty ++ (fields.tail zip values), label)
    }
  }
}

case class NumericDataPoint(fields:Seq[Symbol], values:Seq[Double], label:String) {

  def distance(other:NumericDataPoint):Double = {
    // memoize
    NumericDataPoint.cache +? ((this,other), distance(other.values))
  }

  def distance(other:Seq[Double]):Double = {
    require(values.size == other.size)
    var squares = 0d
    for (i <- values.indices) {
      squares += math.pow(values(i) - other(i), 2)
    }
    math.sqrt(squares)
  }
}

object NumericDataPoint {

  private val cache = collection.mutable.Map.empty[(NumericDataPoint,NumericDataPoint), Double]

  def readFile(path:String) = {
    val (fields, lines) = DataFile.load(path)
    for (d <- lines) yield {
      val label = d.head
      val values = d.tail.map(_.toDouble)
      new NumericDataPoint(fields.tail, values, label)
    }
  }

}



