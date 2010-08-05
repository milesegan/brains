import collection.{ mutable => mut }

class MovieLens(movieFile:io.BufferedSource, ratingFile:io.BufferedSource) {
  
  val userRatings = mut.Map[Int, mut.Map[Int,Double]]()
  val movieRatings = mut.Map[Int, mut.Map[Int,Double]]()
  val similarities = mut.Map[Int,mut.Map[Int,Double]]()
  val separator = """\|"""

  val movies = movieFile.getLines.map { line =>
    val id :: title :: rest = line.split(separator).toList
    id.toInt -> title
  }.toMap

  for (line <- ratingFile.getLines) {
    val user :: movie :: rating :: rest = line.split("""\s""").map(_.toInt).toList
    movieRatings.getOrElseUpdate(movie.toInt, mut.Map[Int,Double]())
    movieRatings(movie)(user) = rating
    userRatings.getOrElseUpdate(user, mut.Map[Int,Double]())
    userRatings(user)(movie) = rating
  }

  def similarity(a:Int, b:Int):Double = {
    val ratingsA = movieRatings.get(a)
    val ratingsB = movieRatings.get(b)
    if (ratingsA.isEmpty || ratingsB.isEmpty) return 0.0

    val meanAllA = mean(ratingsA.get.values.toArray)
    val meanAllB = mean(ratingsB.get.values.toArray)

    // find ratings of both movies by the same users, subtract mean and store deltas
    val commonUsers = movieRatings(a).keySet & movieRatings(b).keySet
    if (commonUsers.size < 3) return 0.0
    val commonRatings = commonUsers.toSeq.map(u => 
      (movieRatings(a)(u) - meanAllA, movieRatings(b)(u) - meanAllB))
    val (commonA, commonB) = commonRatings.unzip

    pearsonCorrelation(commonA.toArray, commonB.toArray)
  }

  def pearsonCorrelation(a:Array[Double], b:Array[Double]):Double = {
    if (a.isEmpty) return 0.0 // no overlapping ratings

    var (meanA, devA) = meanAndDev(a)
    var (meanB, devB) = meanAndDev(b)
    val diffsA = a.map(_ - meanA)
    val diffsB = b.map(_ - meanB)
    val xy = diffsA.zip(diffsB).map(i => i._1 * i._2).sum

    if (devA == 0.0 || devB == 0.0) {
      val sameA = a.forall(_ == a.head)
      val sameB = b.forall(_ == b.head)
      if (sameA && sameB) return 1.0 // degenerate correlation, all points the same

      if (devA == 0) 
        // otherwise either a or b vary
        devA = devB
      else
        devB = devA
    }

    xy / (a.size * devA * devB)
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
