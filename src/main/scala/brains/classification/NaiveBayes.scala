package brains.classification

import brains.Data

/**
 * Classifies sets of points by the naive Bayes method.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Naive_bayes">naive bayes</a>.
 */
class NaiveBayes(trainingSet: Seq[(String, Data.SPoint)])
extends Method(trainingSet) {

  private
  val pm = new ProbabilityMap(trainingSet)

  private def bayesProb(feature: Symbol, value: String, klass: String): Double = {
    val ip = for {pCF <- pm.pCF(feature, value, klass)
                  pC <- pm.pC(klass) 
                  pF <- pm.pF(feature, value)} yield (pCF * pC / pF)
    ip getOrElse 0.1 / trainingSet.size // TODO: adjust this factor
  }

  def classify(point: Data.SPoint): String = {
    val classes = for (c <- pm.classes) yield {
      val p = for ((feature,value) <- point) yield bayesProb(feature, value, c)
      p.reduceLeft(_ * _) -> c
    }
    classes.max._2
  }
}

object NaiveBayes extends Driver {

  def method(trainingData: Seq[(String,Data.SPoint)]) = {
    new NaiveBayes(trainingData)
  }

}
