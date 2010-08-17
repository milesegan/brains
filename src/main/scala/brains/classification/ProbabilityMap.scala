package brains.classification

import brains.data.StringDataPoint
import brains.util._

/**
 * Stores the map of probabilties of features to outcomes for a given
 * dataset.
 */
class ProbabilityMap(data: Seq[StringDataPoint], val outcomeKey: Symbol) {
  import collection.mutable.{ HashMap => HM }
  
  type OP = HM[String,Double] // probabilities of outcomes
  type FP = HM[Symbol,HM[String,Double]] // probabilities of feature values
  type OFP = HM[Symbol,HM[String,HM[String,Double]]] // probabilities of outcomes given feature values

  private val (probO, probF, probOF) = build(data) // outcome, feature O|F probabilities
  
  /**
   * The set of all outcomes seen in the dataset.
   */
  def outcomes = probO.keySet

  /**
   * The most common outcome seen in the dataset.
   */
  def mostCommonOutcome = probO.toSeq.sortBy{ case(o,p) => p }.last._1

  /**
   * The set of all features in the dataset.
   */
  def features = probF.keySet

  /**
   * The set of all features in the dataset.
   */
  def featureValues(feature: Symbol) = probF(feature).keySet

  /**
   * The probability of outcome in the dataset.
   */
  def pO(outcome: String): Option[Double] = probO.get(outcome)

  /**
   * The probability of feature with value in the dataset.
   */
  def pF(feature: Symbol, value: String): Option[Double] = {
    for (x <- probF.get(feature); y <- x.get(value)) yield y
  }

  /**
   * The probability of outcome given feature and value in the
   * dataset.
   */
  def pOF(feature: Symbol, value: String, outcome: String): Option[Double] = {
    for (x <- probOF.get(feature); y <- x.get(value); z <- y.get(outcome)) yield z
  }

  private def build(data: Seq[StringDataPoint]): (OP, FP, OFP) = {
    val o: OP = HM.empty
    val f :FP = HM.empty
    val of: OFP = HM.empty
    for (p <- data) {
      val (outcome, vals) = p.values(outcomeKey) -> (p.values - outcomeKey)
      
      o +? (outcome, 0d)
      o(outcome) += 1

      for ((feature,value) <- vals) {
        f +? (feature, HM.empty)
        f(feature) +? (value, 0d)
        f(feature)(value) += 1

        of +? (feature, HM.empty)
        of(feature) +? (value, HM.empty)
        of(feature)(value) +? (outcome, 0d)
        of(feature)(value)(outcome) += 1
      }
    }

    for ((outcome,count) <- o) { o(outcome) /= data.size }
    for ((feature,values) <- f; (value,count) <- values) {
      values(value) /= data.size
    }
    for ((feature,values) <- of; (value,outcomes) <- values; (outcome,counts) <- outcomes) { 
      outcomes(outcome) /= data.size
    }
    (o, f, of)
  }

}
