package brains.data

import brains.util._

/**
 * Loads data from text files.
 *
 * The files should be in the following format:
 *
 * <ul>
 * <li>Lines beginning with # or containing only whitespace are discarded.
 * <li>The first non-discard line should be a comma separated header listing the names
 * of the features of each datapoint. The first of which will be used as the
 * "label", or unique ID of the field.
 * <li>Following lines should each contain the complete feature values of a
 * a single datapoint, in the same order as the header.
 * </ul>
 *
 * For example:
 * <pre>
 * # sample data set
 * #
 * id,size,weight,height
 * 1,10,150,72
 * 2,12,200,68
 * 3,8,95,60
 * </pre>
 */
object DataFile {

  def load(path: String): (Seq[Symbol],Seq[Seq[String]]) = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val lines = for (i <- src.getLines if !i.matches("""^(#.*|\s*)$""")) yield {
      i.trim.split("""\s?,\s?""").toSeq
    }
    val fields = lines.next.map { Symbol(_) }
    (fields, lines.toSeq)
  }

}

/**
 * A datapoint representing discrete values
 */
case class StringDataPoint(values: Map[Symbol,String], label: String)

object StringDataPoint {

  def readFile(path: String): Seq[StringDataPoint] = {
    val (fields, lines) = DataFile.load(path)
    for (d <- lines) yield {
      val label = d.head
      val values = d.tail
      new StringDataPoint(Map.empty ++ (fields.tail zip values), label)
    }
  }
}

/**
 * A datapoint representing continuous values.
 */
case class NumericDataPoint(fields: Seq[Symbol], values: Seq[Double], label: String) {

  /**
   * Computes the euclidean distance between this point and the values
   * of other.
   */
  def distance(other: NumericDataPoint): Double = {
    // memoize
    NumericDataPoint.cache +? ((this,other), distance(other.values))
  }

  /**
   * Computes the euclidean distance between this point and other.
   */
  def distance(other: Seq[Double]): Double = {
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

  def readFile(path: String) = {
    val (fields, lines) = DataFile.load(path)
    for (d <- lines) yield {
      val label = d.head
      val values = d.tail.map(_.toDouble)
      new NumericDataPoint(fields.tail, values, label)
    }
  }

}



