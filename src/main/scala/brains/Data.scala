package brains

import brains.util._

object Data {

  /**
   * A point made up of string values.
   */
  type SPoint = Map[Symbol,String]
  type SPoints = IndexedSeq[SPoint]

  /**
   * A point made up of numeric values.
   */
  type NPoint = Map[Symbol,Double]
  type NPoints = IndexedSeq[NPoint]

  /**
   * Loads raw data from text files.
   *
   * The files should be in the following format:
   *
   * <ul>
   * <li>Lines beginning with # or containing only whitespace are discarded.
   * <li>Following lines should each contain the complete feature values of a
   * a single datapoint, in the same order as the header.
   * </ul>
   *
   * For example:
   * <pre>
   * # sample data set
   * #
   * size,weight,height
   * 10,150,72
   * 12,200,68
   * 8,95,60
   * </pre>
   */
  def loadStringData(path: String): SPoints = {
    val src = new io.BufferedSource(new java.io.FileInputStream(path))
    val lines = for (i <- src.getLines if !i.matches("""^(#.*|\s*)$""")) yield {
      i.trim.split("""\s?,\s?""").toSeq
    }
    val fields = lines.next.map { Symbol(_) }
    val points = for (i <- lines if i.size == fields.size) yield {
      Map.empty ++ (fields zip i)
    }
    points.toIndexedSeq
  }

  /**
   * Loads raw data and converts values to doubles.
   *
   * File format is the same as that of loadStringData.
   */
  def loadNumberData(path: String): NPoints = {
    val stringData = loadStringData(path)
    stringData.map { p => p.mapValues { _.toDouble } }
  }
  
  /**
   * Extracts feature from all points, returning a sequences of tuples
   * the feature value the new point without the feature.
   */
  def extractFeature[A](feature: Symbol, points: Seq[Map[Symbol,A]]): Seq[(A, Map[Symbol,A])] = {
    for (p <- points) yield {
      (p(feature), p - feature)
    }
  }

  /**
   * Normalize numeric datapoints by subtracting the mean and dividing
   * by the variance for each feature.
   */
  def normalize(points: NPoints): NPoints = {
    val features = points.head.keySet
    val varMeans = for (f <- features) yield {
      val total = points.map(_(f)).sum
      val mean = total / points.size
      val variance = points.map(p => math.pow(p(f) - mean, 2)).sum
      (f, mean, variance)
    }
    for (p <- points) yield {
      val newKV = for ((f, m, v) <- varMeans) yield {
        (f, (p(f) - m) / v)
      }
      Map.empty ++ newKV
    }
  }

}



