package brains.classification

import brains.StringDataPoint

abstract class Driver {

  def method(trainingData:Seq[StringDataPoint], outcomeKey:Symbol):Method

  def main(args:Array[String]) = {
    val outcomeKey = Symbol(args(0))
    val data = util.Random.shuffle(StringDataPoint.readFile(args(1)))
    val (testSet, trainingSet) = data.splitAt(data.size / 3)
    val m = method(trainingSet, outcomeKey)
    println(m)
    var correct, total = 0
    for (t <- testSet) {
      val (outcome, classified) = t.values(outcomeKey) -> m.classify(t)
      total += 1
      if (outcome == classified) correct += 1
      println(outcome + " => " + classified)
    }
    println(correct.toDouble / total)
  }

}
