package brains.classification

import brains.data.StringDataPoint

/**
 * Command line & test driver for classification methods.
 */
abstract class Driver {

  /**
   * Returns the classification to be used by this driver to classify
   * the supplied dataset.
   *
   * @param trainingData The dataset of points to be classified.
   * @param outcomeKey The field in the dataset that corresponds to the correct
   * class for each point.
   */
  def method(trainingData: Seq[StringDataPoint], outcomeKey: Symbol):Method

  /**
   * Classifies the data in a data file.
   *
   * @param args The name of the field corresponding to the outcome in each
   * data point, followed by the name of the file containing the data.
   *
   * For example:
   * 
   * <code>
   * main("poisonous", "data/mushrooms.csv")
   * </code>
   */
  def main(args: Array[String]) = {
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
    println("% correct = " + correct.toDouble / total)
  }

}
