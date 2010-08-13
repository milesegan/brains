package brains.classification

import brains.StringDataPoint

abstract class Classifier(trainingData:Seq[StringDataPoint], val outcomeKey:Symbol) {

  def classify(p:StringDataPoint):String

}


