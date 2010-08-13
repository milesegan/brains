package brains.classification

import brains.StringDataPoint

class NaiveBayesClassifier(trainingSet:Seq[StringDataPoint], outcomeKey:Symbol) 
extends Classifier(trainingSet, outcomeKey) {

  val pm = new ProbabilityMap(trainingSet, outcomeKey)

  private def bayesProb(feature:Symbol, value:String, outcome:String):Double = {
    val ip = for {pOF <- pm.pOF(feature, value, outcome)
                  pO <- pm.pO(outcome) 
                  pF <- pm.pF(feature, value)} yield (pOF * pO / pF)
    ip getOrElse 0.1 / trainingSet.size // TODO: adjust this factor
  }

  def classify(point:StringDataPoint):String = {
    val values = point.values - outcomeKey
    val outcomes = for (outcome <- pm.outcomes) yield {
      val p = for ((feature,value) <- values) yield bayesProb(feature, value, outcome)
      p.reduceLeft(_ * _) -> outcome
    }
    outcomes.max._2
  }
}

object NaiveBayes extends Driver {

  def classifier(trainingData:Seq[StringDataPoint], outcomeKey:Symbol) = {
    new NaiveBayesClassifier(trainingData, outcomeKey)
  }

}
