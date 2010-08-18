package brains.classification

import brains.Data

/**
 * Common interface of all classification methods.
 *
 * @param trainingData The dataset used to build the classifier.
 * @param outcomeKey The field in the dataset that corresponds to the class
 * of each datapoint.
 */
abstract class Method(trainingData: Seq[(String,Data.SPoint)]) {

  /**
   * Classifies a point.
   *
   * @return The string representing the class of point p.
   */
  def classify(p: Data.SPoint):String

}


