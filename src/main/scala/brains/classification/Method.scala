package brains.classification

import brains.data.StringDataPoint

/**
 * Common interface of all classification methods.
 *
 * @param trainingData The dataset used to build the classifier.
 * @param outcomeKey The field in the dataset that corresponds to the class
 * of each datapoint.
 */
abstract class Method(trainingData: Seq[StringDataPoint], val outcomeKey: Symbol) {

  /**
   * Classifies a point.
   *
   * @return The string representing the class of point p.
   */
  def classify(p: StringDataPoint):String

}


