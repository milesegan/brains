package brains.classification

class NeuralNet(val layerDims:Int*) {

  val ErrorThreshold = 0.001d
  val ConvergeoThreshold = 1e-10d
  val LearningRate = 0.25d

  val nLayers = layerDims.size
  val weights = for (i <- 1 until nLayers) yield  {
    val maxInitWeight = 1 / math.sqrt(layerDims(i))
    val nNodes = layerDims(i - 1) + 1 // + 1 for bias nodes
    val nWeights = layerDims(i)
    Array.fill[Double](nNodes, nWeights) { 
      2 * util.Random.nextDouble * maxInitWeight - maxInitWeight 
    } 
  }

  private def calcActivations(input:Array[Double], weights:Array[Array[Double]]):Array[Double] = {
    require(input.size == weights.size - 1)
    val nInputs = input.size
    val nNodes = weights(0).size
    val a = Array.ofDim[Double](nNodes)
    for (i <- 0 until nNodes) {
      a(i) = weights(0)(i) * -1 // bias input
      for (j <- 1 until nInputs) {
        a(i) += input(j) * weights(j)(i)
      }
    }
    a.map { i => 0.5 * (math.tanh(i) + 1) } // activation function
  }

  private def error(a:Array[Double], b:Array[Double]):Double = {
    require(a.size == b.size)
    (a zip b).map { case(a,b) => math.pow(a - b, 2) }.sum
  }

  def classify(input:Array[Double]):Array[Double] = {
    require(input.size == layerDims.head)
    var d = input
    for (i <- 0 until weights.size) {
      d = calcActivations(d, weights(i))
    }
    d
  }

  def train(input:Array[Double], output:Array[Double]) = {
    val calc = classify(input)
    val e = error(calc, output)
  }
}

object NeuralNet {

  def main(args:Array[String]) = {
    val net = new NeuralNet(3, 5, 1)
    val res = net.classify(Array(3, 2, 3))
    println(res.mkString(", "))
  }

}
