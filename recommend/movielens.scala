#!/usr/bin/env scala
!#

import collection.{ mutable => mut }

class MovieLens(userFile:io.BufferedSource, movieFile:io.BufferedSource, ratingFile:io.BufferedSource) {
  
  val userRatings = mut.Map[Int, mut.Map[Int,Int]]()
  val movieRatings = mut.Map[Int, mut.Map[Int,Int]]()
  val movies = mut.Map[Int, String]()

  userFile.getLines.foreach { i =>
    val parts = i.split("::")
    userRatings.getOrElseUpdate(parts(0).toInt, mut.Map[Int,Int]())
  }

  movieFile.getLines.foreach { i =>
    val parts = i.split("::")
    movies(parts(0).toInt) = parts(1)
    movieRatings.getOrElseUpdate(parts(0).toInt, mut.Map[Int,Int]())
  }

  ratingFile.getLines.foreach { i =>
    val parts = i.split("::").map(_.toInt)
    movieRatings(parts(1))(parts(0)) = parts(2)
    userRatings(parts(0))(parts(1)) = parts(2)
  }

  def similarity(a:Int, b:Int):Double = {
    val ratingsA = movieRatings.getOrElse(a, mut.Map[Int,Int]())
    val ratingsB = movieRatings.getOrElse(b, mut.Map[Int,Int]())
    if (ratingsA.isEmpty || ratingsB.isEmpty) return 0.0

    val meanAllA = mean(ratingsA.values.toSeq)
    val meanAllB = mean(ratingsB.values.toSeq)
    var commonA = Seq[Double]()
    var commonB = Seq[Double]()

    // find ratings of other movies by same user
    // subtract mean and store deltas
    movieRatings(a).foreach { kv =>
      movieRatings(b).get(kv._1) match {
        case Some(v) => {
          commonA = commonA :+ (kv._2 - meanAllA)
          commonB = commonB :+ (v - meanAllB)
        }
        case None => ()
      }
    }
    if (commonA.size < 3) return 0.0

    pearsonCorrelation(commonA, commonB)
  }

  def pearsonCorrelation(a:Seq[Double], b:Seq[Double]):Double = {
    if (a.isEmpty) return 0.0 // no overlapping ratings

    var xy = 0.0d
    val meanA = mean(a)
    var devA = standardDeviation(meanA, a)
    val meanB = mean(b)
    var devB = standardDeviation(meanB, b)

    for (i <- 0 until a.size) {
      xy += (a(i) - meanA) * (b(i) - meanB)
    }
    
    if (devA == 0.0 || devB == 0.0) {
      var indA = 0.0
      var indB = 0.0
      (1 until a.size).foreach { i =>
        indA += a(i - 1) - a(i)
        indB += b(i - 1) - b(i)
      }

      if (indA == 0.0 && indB == 0.0) {
        // degenerate correlation, all points the same
        return 1.0
      }
      else if (devA == 0) {
        // otherwise either a or b vary
        devA = devB
      }
      else {
        devB = devA
      }
    }

    xy / (a.size * devA * devB)
  }

  def mean[T <% Double](values:Seq[T]):Double = {
    val total = values.foldLeft(0d)(_ + _)
    total / values.size
  }

  def standardDeviation[T <% Double](mean:Double, values:Seq[T]):Double = {
    val squares = values.map(v => (v - mean) * (v - mean))
    val sum = squares.foldLeft(0d)(_ + _)
    math.sqrt(sum / values.size)
  }
}

def openFile(path:String):io.BufferedSource = {
  new io.BufferedSource(new java.io.FileInputStream(path))
}

def main(args:Array[String]) = {
  val set = new MovieLens(
    openFile(args(0)),
    openFile(args(1)),
    openFile(args(2)))

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

main(args)
