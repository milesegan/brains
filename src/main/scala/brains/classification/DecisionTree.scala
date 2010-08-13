package brains.classification

import brains.StringDataPoint
import annotation.tailrec

class DecisionTreeClassifier(trainingData:Seq[StringDataPoint], outcomeKey:Symbol) 
extends Classifier(trainingData, outcomeKey) {

  sealed abstract class Tree
  case class Leaf(outcome:String) extends Tree
  case class Node(feature:Symbol, branches:Map[String,Tree]) extends Tree

  require(trainingData.nonEmpty)
  
  val pm = new ProbabilityMap(trainingData, outcomeKey)
  val tree = buildTree(trainingData, pm)

  private
  def calcIGain(feature:Symbol, pm:ProbabilityMap):Double = {
    val outcomeEntropies = for (o <- pm.outcomes; p <- pm.pO(o)) yield { entropy(p) }
    val allVEntropies = 
      for (value <- pm.featureValues(feature);
           outcome <- pm.outcomes;
           pV <- pm.pF(feature, value);
           p <- pm.pOF(feature, value, outcome)) yield {
      pV * entropy(p)
   }
             
    outcomeEntropies.sum - allVEntropies.sum
  }

  private
  def buildTree(data:Seq[StringDataPoint], pm:ProbabilityMap):Tree = {
    if (pm.outcomes.size == 1) return Leaf(pm.outcomes.head)

    val gains = for (f <- pm.features) yield (calcIGain(f, pm), f)
    val bestF = gains.toSeq.sortBy(_._1).last._2
    val otherF = pm.features.filter(_ == bestF)
    val children = for (value <- pm.featureValues(bestF)) yield {
      val newData = data.filter{ d => d.values(bestF) == value }
      val newOutcomes = data.map{ d => d.values(outcomeKey) }.toSet
      (value, buildTree(newData, new ProbabilityMap(newData, outcomeKey)))
    }
    Node(bestF, Map.empty ++ children)
  }

  private def entropy(p:Double) = -p * log2(p)

  private def log2(x:Double):Double = math.log(x) / math.log(2d)

  def classify(p:StringDataPoint):String = {

    @tailrec
    def doClassify(p:StringDataPoint, tree:Tree):String = {
      tree match {
        case Leaf(outcome) => outcome
        case Node(feature, branches) => {
          branches.get(p.values(feature)) match {
            case Some(b) => doClassify(p, b)
            case None => pm.mostCommonOutcome
          }
        }
      }
    }

    doClassify(p, tree)
  }
  
  override
  def toString:String = {
    tree.toString
  }
}

object DecisionTree extends Driver {

  def classifier(trainingData:Seq[StringDataPoint], outcomeKey:Symbol) = {
    new DecisionTreeClassifier(trainingData, outcomeKey)
  }

}
