package brains.classification

import brains.Data
import annotation.tailrec

/**
 * Classifies sets of points via a decision tree.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Decision_tree">decision tree</a>.
 */
class DecisionTree(trainingData: Seq[(String,Data.SPoint)])
extends Method(trainingData) {

  sealed abstract class Tree
  case class Leaf(klass: String) extends Tree
  case class Node(feature: Symbol, branches: Map[String,Tree]) extends Tree

  require(trainingData.nonEmpty)
  
  private
  val pm = new ProbabilityMap(trainingData)

  private
  val tree = buildTree(trainingData, pm)

  private
  def calcIGain(feature: Symbol, pm: ProbabilityMap): Double = {
    val classEntropies = for (c <- pm.classes; p <- pm.pC(c)) yield { entropy(p) }
    val allVEntropies = 
      for (value <- pm.featureValues(feature);
           c <- pm.classes;
           pV <- pm.pF(feature, value);
           p <- pm.pCF(feature, value, c)) yield {
      pV * entropy(p)
   }
             
    classEntropies.sum - allVEntropies.sum
  }

  private
  def buildTree(data: Seq[(String,Data.SPoint)], pm: ProbabilityMap): Tree = {
    if (pm.classes.size == 1) return Leaf(pm.mostCommonClass)
    if (data.isEmpty || pm.features.isEmpty) return Leaf(pm.mostCommonClass)

    val gains = pm.features.toSeq.sortBy(calcIGain(_, pm)).reverse
    val bestF = gains.head
    val otherF = gains.tail
    val children = for (value <- pm.featureValues(bestF)) yield {
      val newData = for ((c,d) <- data if d(bestF) == value) yield {
        (c, d - bestF)
      }
      val newClasses = data.map(_._1).toSet
      (value, buildTree(newData, new ProbabilityMap(newData)))
    }
    Node(bestF, Map.empty ++ children)
  }

  private def entropy(p: Double) = -p * log2(p)

  private def log2(x: Double): Double = math.log(x) / math.log(2d)

  def classify(p: Data.SPoint): String = {

    @tailrec
    def doClassify(p: Data.SPoint, tree: Tree): String = {
      tree match {
        case Leaf(klass) => klass
        case Node(feature, branches) => {
          branches.get(p(feature)) match {
            case Some(b) => doClassify(p, b)
            case None => pm.mostCommonClass
          }
        }
      }
    }

    doClassify(p, tree)
  }
  
  override
  def toString: String = {
    tree.toString
  }
}

object DecisionTree extends Driver {

  def method(trainingData: Seq[(String,Data.SPoint)]) = {
    new DecisionTree(trainingData)
  }

}
