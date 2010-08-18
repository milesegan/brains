package brains.classification

import brains.Data
import brains.util._

/**
 * Stores the map of probabilties of features to classes for a given
 * dataset.
 */
class ProbabilityMap(data: Seq[(String,Data.SPoint)]) {
  import collection.mutable.{ HashMap => HM }
  
  type CP = HM[String,Double] // probabilities of classes
  type FP = HM[Symbol,HM[String,Double]] // probabilities of feature values
  type CFP = HM[Symbol,HM[String,HM[String,Double]]] // probabilities of classes given feature values

  private val (probC, probF, probCF) = build(data) // class, feature O|F probabilities
  
  /**
   * The set of all classes seen in the dataset.
   */
  def classes = probC.keySet

  /**
   * The most common class seen in the dataset.
   */
  def mostCommonClass = probC.toSeq.sortBy{ case(c,p) => p }.last._1

  /**
   * The set of all features in the dataset.
   */
  def features = probF.keySet

  /**
   * The set of all features in the dataset.
   */
  def featureValues(feature: Symbol) = probF(feature).keySet

  /**
   * The probability of class in the dataset.
   */
  def pC(klass: String): Option[Double] = probC.get(klass)

  /**
   * The probability of feature with value in the dataset.
   */
  def pF(feature: Symbol, value: String): Option[Double] = {
    for (x <- probF.get(feature); y <- x.get(value)) yield y
  }

  /**
   * The probability of class given feature and value in the
   * dataset.
   */
  def pCF(feature: Symbol, value: String, klass: String): Option[Double] = {
    for (x <- probCF.get(feature); y <- x.get(value); z <- y.get(klass)) yield z
  }

  private def build(data: Seq[(String,Data.SPoint)]): (CP, FP, CFP) = {
    val c: CP = HM.empty
    val f :FP = HM.empty
    val cf: CFP = HM.empty
    for ((klass, vals) <- data) {
      c +? (klass, 0d)
      c(klass) += 1

      for ((feature,value) <- vals) {
        f +? (feature, HM.empty)
        f(feature) +? (value, 0d)
        f(feature)(value) += 1

        cf +? (feature, HM.empty)
        cf(feature) +? (value, HM.empty)
        cf(feature)(value) +? (klass, 0d)
        cf(feature)(value)(klass) += 1
      }
    }

    for ((klass,count) <- c) { c(klass) /= data.size }
    for ((feature,values) <- f; (value,count) <- values) {
      values(value) /= data.size
    }
    for ((feature,values) <- cf; (value,classes) <- values; (klass,counts) <- classes) { 
      classes(klass) /= data.size
    }
    (c, f, cf)
  }

}
