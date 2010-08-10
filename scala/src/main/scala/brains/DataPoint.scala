package brains

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

class NumericDataPoint(values:Map[Symbol,Double], label:String) extends DataPoint[Double](values, label) {

  def distance(other:NumericDataPoint):Double = {
    // memoize
    NumericDataPoint.cache.getOrElseUpdate((this,other), distance(other.values))
  }

  def distance(other:Map[Symbol,Double]):Double = {
    val squares = values.map { case (k,v) => 
      math.pow(v - other.getOrElse(k, 0d), 2)
    }
    math.sqrt(squares.sum)
  }
}

object NumericDataPoint {
  private val cache = collection.mutable.Map.empty[(NumericDataPoint,NumericDataPoint), Double]

  def readFile(path:String) = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val dataLines = src.getLines.map { _.split("""\s?,\s?""").toList }
    val fields = dataLines.next.map { Symbol(_) }
    dataLines.map { d =>
      val label = d.head
      val doubles = d.tail.map(_.toDouble)
      new NumericDataPoint(Map.empty ++ (fields zip doubles), label)
    }
  }

}

