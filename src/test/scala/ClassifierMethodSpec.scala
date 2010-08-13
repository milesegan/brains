import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import brains.StringDataPoint
import brains.classification.{Method,DecisionTree,NaiveBayes,NeuralNet}

class ClassificationMethodSpec extends Spec with ShouldMatchers {

  def runMethod(m:Method, data:Seq[StringDataPoint]):Double = {
    val results = for (d <- data) yield {
      d.values(m.outcomeKey) -> m.classify(d)
    }
    val correct = results.filter{ case(a,b) => a == b }
    correct.size / results.size.toDouble
  }

  describe("ClassificationMethods") {

    val data = util.Random.shuffle(StringDataPoint.readFile("data/mushroom.csv"))
    val (testData, trainData) = data.splitAt(data.size / 4)

    describe("DecisionTree") {
      val m = new DecisionTree(trainData, 'poisonous)

      it ("should classify well") {
        runMethod(m, testData) should be >= 0.9
      }
    }

    describe("NaiveBayes") {
      val m = new NaiveBayes(trainData, 'poisonous)

      it ("should classify well") {
        runMethod(m, testData) should be >= 0.9
      }
    }

    // TODO: add neural net test
  }

}

