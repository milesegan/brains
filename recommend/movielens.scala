import collection.immutable.HashMap
import collection.mutable.{ Map => MMap }

class MovieLens(movieFile:io.BufferedSource, ratingFile:io.BufferedSource) {
  
  val separator = """\|"""

  val movies = movieFile.getLines.map { line =>
    val id :: title :: rest = line.split(separator).toList
    id.toInt -> title
  }.toMap

  val (userRatings, movieRatings) = {
    val userBuilder = MMap[Int,MMap[Int,Double]]()
    val movieBuilder = MMap[Int,MMap[Int,Double]]()
    
    for (line <- ratingFile.getLines) {
      val user :: movie :: rating :: rest = line.split("""\s""").map(_.toInt).toList
      movieBuilder.getOrElseUpdate(movie.toInt, MMap[Int,Double]())
      movieBuilder(movie)(user) = rating
      userBuilder.getOrElseUpdate(user, MMap[Int,Double]())
      userBuilder(user)(movie) = rating
    }
    (userBuilder.toMap, movieBuilder.toMap)
  }

  def similarity(a:Int, b:Int):Double = {
    val ratingsA = movieRatings.get(a).getOrElse { return 0.0 }
    val ratingsB = movieRatings.get(b).getOrElse { return 0.0 }

    val meanAllA = mean(ratingsA.values.toArray)
    val meanAllB = mean(ratingsB.values.toArray)

    // find ratings of both movies by the same users, subtract mean and store deltas
    val commonUsers = ratingsA.keySet & ratingsB.keySet
    if (commonUsers.size < 3) return 0.0
    val commonRatings = commonUsers.toSeq.map(u => (ratingsA(u) - meanAllA, ratingsB(u) - meanAllB))
    val (commonA, commonB) = commonRatings.unzip

    pearson(commonA.toArray, commonB.toArray)
  }

  def pearson(a:Array[Double], b:Array[Double]):Double = {
    if (a.isEmpty) return 0.0 // no overlapping ratings

    var (meanA, devA) = meanAndDev(a)
    var (meanB, devB) = meanAndDev(b)
    val xy = (a zip b).map{ case (a,b) => (a - meanA) * (b - meanB) }.sum

    (devA, devB) match {
      case (0.0, 0.0) => return 1.0 // degenerate correlation, all points the same
      case (0.0, devB) => xy / (a.size * devB * devB)
      case (devA, 0.0) => xy / (a.size * devA * devA)
      case (devA, devB) => xy / (a.size * devA * devB)
    }
  }

  def mean(values:Array[Double]):Double = values.sum / values.size

  def meanAndDev(values:Array[Double]):(Double,Double) = {
    val meanV = mean(values)
    val squares = values.map(v => (v - meanV) * (v - meanV)).reduceLeft(_ + _)
    (meanV, math.sqrt(squares / values.size))
  }
}

object MovieLens {
  def openFile(path:String) = new io.BufferedSource(new java.io.FileInputStream(path))

  def main(args:Array[String]) = {
    val set = new MovieLens(openFile(args(0)), openFile(args(1)))
    val movies = set.movies.keySet.toSeq
    val sims = for (m <- movies) yield (set.similarity(86,m), set.movies(m))
    for ((sim, title) <- sims.sorted) {          
      printf("%3f %s\n", sim, title)
    }
  }
}
