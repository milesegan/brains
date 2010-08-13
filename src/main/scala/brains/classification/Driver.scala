package brains.classification

import brains.StringDataPoint

trait Driver {

  def classifier(trainingData:Seq[StringDataPoint], outcomeKey:Symbol):Classifier

  def main(args:Array[String]) = {
    val outcomeKey = Symbol(args(0))
    val data = util.Random.shuffle(StringDataPoint.readFile(args(1)))
    val (testSet, trainingSet) = data.splitAt(data.size / 3)
    val c = classifier(trainingSet, outcomeKey)
    println(c)
    var correct, total = 0
    for (t <- testSet) {
      val (outcome, classified) = t.values(outcomeKey) -> c.classify(t)
      total += 1
      if (outcome == classified) correct += 1
      println(outcome + " => " + classified)
    }
    println(correct.toDouble / total)
  }

}
