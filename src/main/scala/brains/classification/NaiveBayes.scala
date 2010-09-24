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

  def classify(point: Data.SPoint): String = {
    val classes = for (c <- pm.classes) yield {
      val p = for ((feature,value) <- point) yield {
        val pCF = pm.pCF(feature, value, c)
        pCF getOrElse 0.05 / trainingSet.size // TODO: adjust this factor
      }
      pm.pC(c).getOrElse(0d) * p.product / trainingSet.size -> c
    }
    classes.max._2
  }
}

object NaiveBayes extends Driver {

  def method(trainingData: Seq[(String,Data.SPoint)]) = {
    new NaiveBayes(trainingData)
  }

}
