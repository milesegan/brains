import collection.{ mutable => mut }

class MovieLens(movieFile:io.BufferedSource, ratingFile:io.BufferedSource) {
  
  val userRatings = mut.Map[Int, mut.Map[Int,Double]]()
  val movieRatings = mut.Map[Int, mut.Map[Int,Double]]()
  val movies = mut.Map[Int, String]()
  val similarities = mut.Map[Int,mut.Map[Int,Double]]()
  val separator = """\|"""

  for (line <- movieFile.getLines) {
    val num :: name :: rest = line.split(separator).toList
    movies(num.toInt) = name
    movieRatings.getOrElseUpdate(num.toInt, mut.Map[Int,Double]())
  }

  for (line <- ratingFile.getLines) {
    val user :: movie :: rating :: rest = line.split("""\s""").map(_.toInt).toList
    movieRatings(movie)(user) = rating
    userRatings.getOrElseUpdate(user, mut.Map[Int,Double]())
    userRatings(user)(movie) = rating
  }

  def similarity(a:Int, b:Int):Double = {
    val ratingsA = movieRatings.getOrElse(a, mut.Map[Int,Double]())
    val ratingsB = movieRatings.getOrElse(b, mut.Map[Int,Double]())
    if (ratingsA.isEmpty || ratingsB.isEmpty) return 0.0

    val meanAllA = mean(ratingsA.values.toArray)
    val meanAllB = mean(ratingsB.values.toArray)

    // find ratings of other movies by same user, subtract mean and store deltas
    val commons = for { (user,rating) <- movieRatings(a)
                        bRating <- movieRatings(b).get(user) 
                     } yield (rating, bRating)
    val (commonA, commonB) = commons.unzip
    if (commonA.size < 3) return 0.0

    pearsonCorrelation(commonA.toArray, commonB.toArray)
  }

  def pearsonCorrelation(a:Array[Double], b:Array[Double]):Double = {
    if (a.isEmpty) return 0.0 // no overlapping ratings

    var (meanA, devA) = meanAndDev(a)
    var (meanB, devB) = meanAndDev(b)
    val diffsA = a.map(_ - meanA)
    val diffsB = b.map(_ - meanB)
    val xy = diffsA.zip(diffsB).map(i => i._1 * i._2).reduceLeft(_ + _)

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

  def mean(values:Array[Double]):Double = values.foldLeft(0d)(_ + _) / values.size

  def meanAndDev(values:Array[Double]):(Double,Double) = {
    val meanV = mean(values)
    val squares = values.map(v => (v - meanV) * (v - meanV)).reduceLeft(_ + _)
    (meanV, math.sqrt(squares / values.size))
  }
}

object MovieLens {
  def openFile(path:String):io.BufferedSource = {
    new io.BufferedSource(new java.io.FileInputStream(path))
  }

  def main(args:Array[String]) = {
    val set = new MovieLens(openFile(args(0)), openFile(args(1)))

    val movies = set.movies.keySet
    var sims = Seq[Tuple2[Double,String]]()
    movies.foreach { m =>
      val sim = set.similarity(86, m)
                    sims = sims :+ (sim, set.movies(m))
                  }
    sims.sorted.foreach { i =>
      printf("%3f %s\n", i._1, i._2)
    }
  }
}
