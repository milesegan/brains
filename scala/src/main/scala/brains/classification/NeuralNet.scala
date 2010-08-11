package brains.classification

class NeuralNet(val layerDims:Int*) {

  val ErrorThreshold = 0.001d
  val ConvergeoThreshold = 1e-10d
  val LearningRate = 0.25d

  val nLayers = layerDims.size
  val weights = for (i <- 1 until nLayers) yield  {
    val maxInitWeight = 1 / math.sqrt(layerDims(i))
    val nNodes = layerDims(i - 1) + 1 // + 1 for bias nodes, which are at the end
    val nWeights = layerDims(i)
    Array.fill[Double](nNodes, nWeights) { 
      2 * util.Random.nextDouble * maxInitWeight - maxInitWeight 
    } 
  }

  private def multMatrix(v:Array[Double], m:Array[Array[Double]]):Array[Double] = {
    require(m.size == v.size)
    val newN = m(0).size
    val a = Array.ofDim[Double](newN)
    for (i <- 0 until newN) {
      for (j <- 0 until v.size) {
        a(i) += v(j) * m(j)(i)
      }
    }
    a
  }

  private def error(a:Array[Double], b:Array[Double]):Double = {
    require(a.size == b.size)
    (a zip b).map { case(a,b) => math.pow(a - b, 2) }.sum
  }

  private def activation(v:Double) = math.tanh(v)

  def classify(input:Array[Double]):Array[Double] = {
    require(input.size == layerDims.head)
    var d = input
    for (i <- 0 until weights.size) {
      val dWithBias = d :+ -1d
      d = multMatrix(dWithBias, weights(i))
      for (i <- 0 until d.size) { d(i) = activation(d(i)) } // activation function
    }
    d
  }

  def train(input:Array[Double], output:Array[Double]) = {
    //val calc = classify(input)
    //val oErr = for (i <- 0 until output.size) yield (output(i) - calc(i)) * calc(i) * (1 - calc(i))
  }
}

object NeuralNet {

  def main(args:Array[String]) = {
    val net = new NeuralNet(3, 5, 1)
    net.train(Array(0, 1, 1), Array(1))
    net.train(Array(1, 0, 1), Array(0))
    val res = net.classify(Array(1, 0, 0))
    println(res.mkString(", "))
  }

}
