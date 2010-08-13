import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import brains.NumericDataPoint
import brains.StringDataPoint

class DataPointSpec extends Spec with ShouldMatchers {

  describe("NumericDataPoint") {

    val testData = NumericDataPoint.readFile("data/iris-data.csv")
    it ("should contain data") {
      testData.size should be > 0
    }

  }

  describe("StringDataPoint") {

    val testData = StringDataPoint.readFile("data/agaricus-lepiota.data")
    it ("should contain data") {
      testData.size should be > 0
    }

  }

}

