import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import brains.data.NumericDataPoint
import brains.clustering.{KMeans, SingleLink, SpanningTree}

class ClusterMethodSpec extends Spec with ShouldMatchers {

  describe("ClusterMethods") {

    val testData = NumericDataPoint.readFile("data/iris.csv")

    describe("KMeans") {
      val m = new KMeans
      val clusters = m.cluster(4, testData)

      it ("should create clusters") {
        clusters.size should equal (4)
      }
    }

    describe("SingleLink") {
      val m = new SingleLink
      val clusters = m.cluster(4, testData)

      it ("should create clusters") {
        clusters.size should be <= (4)
      }
    }

    // TODO: add spanning tree test
  }

}

