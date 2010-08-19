package brains.clustering

import brains.Data
import annotation.tailrec

/**
 * Clusters numeric datasets via the self organizing map method.
 * @see <a href="http://en.wikipedia.org/wiki/Self_organizing_map">self organizing map</a>.
 */
class SelfOrganizingMap extends Method {

  def cluster(k: Int, points: Cluster): Clusters = {
    require(k > 1)
    require(points.nonEmpty)

    val features = points.head.keys.toSeq
    var w = for (i <- 0 until k) yield {
      Map.empty ++ features.map(f => f -> util.Random.nextDouble)
    }
    val maxI = 1000 // maxinum number of iterations
    val (dI, dF) = (0.5, 0.05) // initial and final neighbor distance
    val (etaI, etaF) = (0.3, 0.03) // inital and final learning rate
    val (etaNI, etaNF) = (0.1, 0.01) // initial and final neighbor learning rate
    val clusters: Clusters = Seq()

    var (eta, etaN, d) = (etaI, etaNI, dI)
    var i = 0
    while (i < maxI && eta > etaF && etaN > etaNF) {
      for (p <- points.indices) {
        // find distances from this point to all nodes
        val sortedN = w.sortBy(i => distance(points(p), i))
        val bestN = sortedN.head
        val otherN = sortedN.tail

        // update closest node
        val newBestN = for ((k,v) <- bestN) yield { 
          (k, v + eta * (points(p)(k) - v)) 
        }

        // update other nodes
        val newOtherN = for (n <- otherN) yield {
          for ((k, v) <- n) yield {
            val h = if (distance(n, bestN) < d) 1.0 else 0.0
            (k, v + etaN * h * (points(p)(k) -v))
          }
        }
        val newW = newBestN +: newOtherN
      }
      eta = etaI * math.pow(etaF / etaI, i / maxI.toDouble)
      etaN = etaNI * math.pow(etaNF / etaNI, i / maxI.toDouble)
      d = dI * math.pow(dF / dI, i / maxI.toDouble)
      i += 1
      println("eta %f, etaN %f, d %f, i %d".format(eta, etaN, d, i))
    }

    println(w)
    Seq()
  }

}

object SelfOrganizingMap extends Driver[SelfOrganizingMap]
