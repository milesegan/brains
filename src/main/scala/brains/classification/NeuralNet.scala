package brains.classification

import brains.Data

/**
 * Single-layer neural network implementation.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Neural_net">neural net</a>.
 */
class NeuralNet(val nInputs: Int, val nHidden: Int, val nOutputs: Int) {

  private val Beta = 1d
  private val Eta = 0.1d

  private
  val hWeights = Array.fill[Double](nInputs + 1, nHidden) { randInitWeight } // +1 for bias node, weight at end

  private
  val oWeights = Array.fill[Double](nHidden + 1, nOutputs) { randInitWeight } // +1 for bias node

  private def randInitWeight: Double = { 
    val maxInitWeight = 1 / math.sqrt(nHidden)
    util.Random.nextDouble * 2 * maxInitWeight - maxInitWeight
  }

  private def multMatrix(v: Array[Double], m: Array[Array[Double]]): Array[Double] = {
    require(m.size == v.size)
    val newN = m(0).size
    val a = Array.ofDim[Double](newN)
    for (i <- 0 until newN) {
      for (j <- v.indices) {
        a(i) += v(j) * m(j)(i)
      }
    }
    a
  }

  private def error(a: Array[Double], b: Array[Double]):Double = {
    require(a.size == b.size)
    (a zip b).map { case(a,b) => math.pow(a - b, 2) }.sum
  }

  private def activation(v: Double) = 1.0d / (1 + math.exp(-Beta * v)) // sigmoid activation

  // Classifies point, returning both outputs and internal weights.
  private def doClassify(input: Array[Double]): (Array[Double], Array[Double]) = {
    require(input.size == nInputs)
    val hAct = multMatrix(input :+ -1d, hWeights) map activation // add -1d for bias node
    val oAct = multMatrix(hAct :+ -1d, oWeights) map activation
    hAct -> oAct
  }

  /**
   * Classifies the input with weights learned during training.
   *
   * @return The output of the network for the given input.
   */
  def classify(input: Array[Double]): Array[Double] = {
    require(input.size == nInputs)
    val (hAct, oAct) = doClassify(input)
    oAct
  }

  /**
   * Trains the network, updating weights.
   *
   * @param input The array of numeric values going in to the network.
   * @param outpt The correct output for the given input.
   */
  def train(input: Array[Double], output: Array[Double]) = {
    val (hAct, oAct) = doClassify(input)

    // calc output error
    val oErr = for (k <- 0 until nOutputs) yield {
      (output(k) - oAct(k)) * oAct(k) * (1 - oAct(k)) // derivative of activation function
    }

    // calc hidden layer error
    val hErr = for (j <- 0 until nHidden) yield {
      val errProd = for (k <- 0 until nOutputs) yield oWeights(j)(k) * oErr(k)
      hAct(j) * (1 - hAct(j)) * errProd.sum
    }
    
    // update output weights
    for (j <- 0 until nHidden; k <- 0 until nOutputs) {
      oWeights(j)(k) += Eta * oErr(k) * hAct(j)
    }

    // update hidden layer weights
    for (i <- 0 until nInputs; j <- 0 until nHidden) {
      hWeights(i)(j) += Eta * hErr(j) * input(i)
    }
  }
}

object NeuralNet {

  /**
   * TODO: document this
   */
  def main(args: Array[String]) = {
    val klass = Symbol(args(0))
    val rawData = Data.loadNumberData(args(1))
    val mappedData = for (i <- rawData) yield {
      val target = for (j <- 0 to 2) yield { if (i(klass) == j) 1.d else 0d }
      target.toArray -> (i - klass)
    }
    val shuffledData = util.Random.shuffle(mappedData.toSeq)
    val (testSet, trainingSet) = shuffledData.splitAt(shuffledData.size / 4)
    val net = new NeuralNet(4, 5, 3)
    for (i <- 0 until 100) {
      for ((target,point) <- trainingSet) net.train(point.values.toArray, target)
    }
    for ((target,point) <- testSet) {
      val oAct = net.classify(point.values.toArray)
      println(target.mkString(", ") + " -> " + oAct.mkString(", "))
    }
  }

}
