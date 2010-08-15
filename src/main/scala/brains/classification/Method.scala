package brains.classification

import brains.StringDataPoint

abstract class Method(trainingData: Seq[StringDataPoint], val outcomeKey: Symbol) {

  def classify(p: StringDataPoint):String

}


