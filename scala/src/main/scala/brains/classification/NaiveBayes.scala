package brains.classification

import brains.StringDataPoint

import annotation.tailrec

class NaiveBayes(val trainingSet:Set[StringDataPoint], val conceptKey:Symbol) {

  type CP = Map[String,Double] // probabilities of concepts
  type IP = Map[(Symbol,String),Double] // probabilities of instances
  type CIP = Map[(Symbol,String),Map[String,Double]] // probabilities of concepts given instances

  val (pC, pI, pCI) = train(trainingSet) // concept, instance, C|I probabilities

  private def train(data:Set[StringDataPoint]):(CP, IP, CIP) = {
    import collection.mutable.{ HashMap => HM }
    val c = HM.empty[String,Double]
    val a = HM.empty[(Symbol,String),Double]
    val ac = HM.empty[(Symbol,String),HM[String,Double]]
    for (p <- data) {
      val (concept, vals) = (p.values(conceptKey), p.values - conceptKey)
      c.getOrElseUpdate(concept, 0d)
      c(concept) += 1
      for ((k,v) <- vals) {
        a.getOrElseUpdate((k,v), 0d)
        a((k,v)) += 1
        ac.getOrElseUpdate((k,v), HM.empty)
        ac((k,v)).getOrElseUpdate(concept, 0d)
        ac((k,v))(concept) += 1
      }
    }
    for ((k,v) <- c) { c(k) = v / c.values.sum }
    for ((k,v) <- a) { a(k) = v / a.values.sum }
    for ((k,v) <- ac; (concept,count) <- v) { v(concept) /= v.values.sum }
    (c.toMap, a.toMap, ac.map { case(k,v) => (k, v.toMap) }.toMap)
  }

  def classify(point:StringDataPoint):String = {
    val values = point.values - conceptKey
    val concepts = 
      for (c <- pC.keySet) yield {
        val p = for (av <- values) yield {
          if (pCI.contains(av) && pCI(av).contains(c)) {
            pCI(av)(c) * pC(c) / pI(av)
          }
          else 0.1 / trainingSet.size // TODO: adjust this
        }
        (p.reduceLeft(_ * _), c)
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
      val (concept, classified) = (t.values(conceptKey), classifer.classify(t))
      total += 1
      if (concept == classified) correct += 1
      println(concept + " => " + classified)
    }
    println(correct.toDouble / total)
  }
}
