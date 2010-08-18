package brains.classification

import brains.Data

/**
 * Command line & test driver for classification methods.
 */
abstract class Driver {

  /**
   * Returns the classification to be used by this driver to classify
   * the supplied dataset.
   *
   * @param trainingData Tuples of (class, point)
   */
  def method(trainingData: Seq[(String,Data.SPoint)]): Method

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
    val classKey = Symbol(args(0))
    val data = util.Random.shuffle(Data.loadStringData(args(1)))
    val mappedData = Data.extractFeature(classKey, data)
    val (testSet, trainingSet) = mappedData.splitAt(mappedData.size / 4)
    val m = method(trainingSet)
    var correct, total = 0
    for ((c, p) <- testSet) {
      val classified = m.classify(p)
      total += 1
      if (c == classified) correct += 1
      println(c + " => " + classified)
    }
    println("% correct = " + correct.toDouble / total)
  }

}
