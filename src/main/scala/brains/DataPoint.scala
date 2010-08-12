package brains

import brains.util._

abstract class DataPoint[T](val values:Map[Symbol,T], val label:String)

class StringDataPoint(values:Map[Symbol,String], label:String) extends DataPoint[String](values, label)

object StringDataPoint {
  def readFile(path:String) = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val dataLines = src.getLines.map { _.trim.split("""\s?,\s?""").toList }
    val fields = dataLines.next.tail.map { Symbol(_) }
    dataLines.map { d =>
      val label = d.head
      val values = d.tail
      new StringDataPoint(Map.empty ++ (fields zip values), label)
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
    for (i <- 0 until values.size) {
      squares += math.pow(values(i) - other(i), 2)
    }
    math.sqrt(squares)
  }
}

object NumericDataPoint {
  private val cache = collection.mutable.Map.empty[(NumericDataPoint,NumericDataPoint), Double]

  def readFile(path:String) = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val dataLines = for (i <- src.getLines if !i.matches("""^(#.+|\s*)$""")) yield {
      i.split("""\s?,\s?""")
    }
    val fields = dataLines.next.map { Symbol(_) }
    for (d <- dataLines) yield {
      val label = d.head
      val values = d.tail.map(_.toDouble)
      new NumericDataPoint(fields, values, label)
    }
  }

}

