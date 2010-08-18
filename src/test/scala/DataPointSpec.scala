import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import brains.Data

class DataPointSpec extends Spec with ShouldMatchers {

  describe("Numeric Data") {

    val testData = Data.loadNumberData("data/iris.csv")
    it ("should contain data") {
      testData.size should be > 0
    }

  }

  describe("String Data") {

    val testData = Data.loadStringData("data/mushroom.csv")
    it ("should contain data") {
      testData.size should be > 0
    }

  }

}

