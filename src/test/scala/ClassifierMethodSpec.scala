import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import brains.Data
import brains.classification.{Method,DecisionTree,NaiveBayes,NeuralNet}

class ClassificationMethodSpec extends Spec with ShouldMatchers {

  def runMethod(m:Method, data:Seq[(String,Data.SPoint)]):Double = {
    val results = for ((c,d) <- data) yield {
      c -> m.classify(d)
    }
    val correct = results.filter{ case(a,b) => a == b }
    correct.size / results.size.toDouble
  }

  describe("ClassificationMethods") {

    val data = util.Random.shuffle[Data.SPoint,IndexedSeq](Data.loadStringData("data/mushroom.csv"))
    val classifiedData = Data.extractFeature('poisonous, data)
    val (testData, trainData) = classifiedData.splitAt(data.size / 4)

    describe("DecisionTree") {
      val m = new DecisionTree(trainData)

      it ("should classify well") {
        runMethod(m, testData) should be >= 0.9
      }
    }

    describe("NaiveBayes") {
      val m = new NaiveBayes(trainData)

      it ("should classify well") {
        runMethod(m, testData) should be >= 0.9
      }
    }

    // TODO: add neural net test
  }

}

