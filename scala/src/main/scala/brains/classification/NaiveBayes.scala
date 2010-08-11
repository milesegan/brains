package brains.classification

import brains.StringDataPoint
import brains.util._

import annotation.tailrec

class NaiveBayes(val trainingSet:Set[StringDataPoint], val conceptKey:Symbol) {

  type CP = Map[String,Double] // probabilities of concepts
  type IP = Map[Symbol,Map[String,Double]] // probabilities of instances
  type CIP = Map[(Symbol,String),Map[String,Double]] // probabilities of concepts given instances

  val (pC, pI, pCI) = train(trainingSet) // concept, instance, C|I probabilities

  private def train(data:Set[StringDataPoint]):(CP, IP, CIP) = {
    import collection.mutable.{ HashMap => HM }
    val c = HM.empty[String,Double]
    val i = HM.empty[Symbol,HM[String,Double]]
    val ci = HM.empty[(Symbol,String),HM[String,Double]]
    for (p <- data) {
      val (concept, vals) = p.values(conceptKey) -> (p.values - conceptKey)
      c.getOrElseUpdate(concept, 0d)
      c(concept) += 1
      for ((k,v) <- vals) {
        i ||+ (k, HM.empty)
        i(k) ||+ (v, 0d)
        i(k)(v) += 1
        ci ||+ (k -> v, HM.empty)
        ci(k -> v) ||+ (concept, 0d)
        ci(k -> v)(concept) += 1
      }
    }
    for ((k,v) <- c) { c(k) = v / c.values.sum }
    for ((k,v) <- i; (iv,count) <- v) { v(iv) /= v.values.sum }
    for ((k,v) <- ci; (concept,count) <- v) { v(concept) /= v.values.sum }
    (c.toMap, 
     i.map { case(k,v) => (k, v.toMap) }.toMap,
     ci.map { case(k,v) => (k, v.toMap) }.toMap)
  }

  def classify(point:StringDataPoint):String = {
    val values = point.values - conceptKey
    val concepts = 
      for (c <- pC.keySet) yield {
        val p = for ((i,v) <- values) yield {
          if (pCI.contains(i -> v) && pCI(i -> v).contains(c)) {
            pCI(i -> v)(c) * pC(c) / pI(i)(v)
          }
          else 0.1 / trainingSet.size // TODO: adjust this
        }
        p.reduceLeft(_ * _) -> c
      }

    concepts.max._2
  }
}

object NaiveBayes {
  def main(args:Array[String]) = {
    val conceptKey = Symbol(args(0))
    val data = StringDataPoint.readFile(args(1)).toSeq
    val (testSet, trainingSet) = data.splitAt(data.size / 3)
    val classifer = new NaiveBayes(trainingSet.toSet, conceptKey)
    var correct, total = 0
    for (t <- testSet) {
      val (concept, classified) = t.values(conceptKey) -> classifer.classify(t)
      total += 1
      if (concept == classified) correct += 1
      println(concept + " => " + classified)
    }
    println(correct.toDouble / total)
  }
}
