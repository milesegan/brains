#!/usr/bin/env clj

(ns movielens
  (import java.io.BufferedReader
          java.io.FileReader)
  (require [clojure.string :as str]))

(defn read-file [file delim]
  (let [rdr (-> file (FileReader.) (BufferedReader.))
        lines (line-seq rdr)]
    (doall
     (map #(str/split % delim) lines))))

(let [movies (read-file (nth *command-line-args* 1) #"\|")
      movies (reduce (fn [a b]
                       (assoc a (nth b 0) (nth b 1)))
                     {}
                     movies)
      ratings (read-file (nth *command-line-args* 2) #"\s")]
  (println movies))

;; import collection.{ mutable => mut }

;; class MovieLens(userFile:io.BufferedSource, movieFile:io.BufferedSource, ratingFile:io.BufferedSource) {
  
;;   val userRatings = mut.Map[Int, mut.Map[Int,Double]]()
;;   val movieRatings = mut.Map[Int, mut.Map[Int,Double]]()
;;   val movies = mut.Map[Int, String]()
;;   val similarities = mut.Map[Int,mut.Map[Int,Double]]()
;;   val separator = """\|"""

;;   userFile.getLines.foreach { i =>
;;     val parts = i.split(separator)
;;     userRatings.getOrElseUpdate(parts(0).toInt, mut.Map[Int,Double]())
;;   }

;;   movieFile.getLines.foreach { i =>
;;     val parts = i.split(separator)
;;     movies(parts(0).toInt) = parts(1)
;;     movieRatings.getOrElseUpdate(parts(0).toInt, mut.Map[Int,Double]())
;;   }

;;   ratingFile.getLines.foreach { i =>
;;     val parts = i.split("""\s""").map(_.toInt)
;;     movieRatings(parts(1))(parts(0)) = parts(2).toDouble
;;     userRatings(parts(0))(parts(1)) = parts(2).toDouble
;;   }

;;   /*
;;   println(new java.util.Date)
;;   for (i <- 0 until movies.keySet.size) {
;;     similarities(i) = mut.Map[Int,Double]()
;;     for (j <- i until movies.keySet.size) {
;;       if (i == j) {
;;         similarities(i)(j) = 1
;;       }
;;       else {
;;         similarities(i)(j) = similarity(i, j)
;;       }
;;     }
;;   }
;;   println(new java.util.Date)
;;   */

;;   def similarity(a:Int, b:Int):Double = {
;;     val ratingsA = movieRatings.getOrElse(a, mut.Map[Int,Double]())
;;     val ratingsB = movieRatings.getOrElse(b, mut.Map[Int,Double]())
;;     if (ratingsA.isEmpty || ratingsB.isEmpty) return 0.0

;;     val meanAllA = mean(ratingsA.values.toArray)
;;     val meanAllB = mean(ratingsB.values.toArray)
;;     var commonA = Array[Double]()
;;     var commonB = Array[Double]()

;;     // find ratings of other movies by same user
;;     // subtract mean and store deltas
;;     movieRatings(a).foreach { case(user, rating) =>
;;       movieRatings(b).get(user) match {
;;         case Some(v) => {
;;           commonA = commonA :+ (rating - meanAllA)
;;           commonB = commonB :+ (v - meanAllB)
;;         }
;;         case None => ()
;;       }
;;     }
;;     if (commonA.size < 3) 0.0 else pearsonCorrelation(commonA, commonB)
;;   }

;;   def pearsonCorrelation(a:Array[Double], b:Array[Double]):Double = {
;;     if (a.isEmpty) return 0.0 // no overlapping ratings

;;     val meanA = mean(a)
;;     var devA = standardDeviation(meanA, a)
;;     val meanB = mean(b)
;;     var devB = standardDeviation(meanB, b)
;;     val adiffs = a.map(_ - meanA)
;;     val bdiffs = b.map(_ - meanB)
;;     val xy = adiffs.zip(bdiffs).map(i => i._1 * i._2).reduceLeft(_ + _)
    
;;     if (devA == 0.0 || devB == 0.0) {
;;       val allA = a.forall(_ == a.head)
;;       val allB = b.forall(_ == b.head)
;;       if (allA && allB) return 0.0 // degenerate correlation, all points the same
;;       // otherwise either a or b vary
;;       if (devA == 0) devA = devB else devB = devA
;;     }

;;     xy / (a.size * devA * devB)
;;   }

;;   def mean(values:Array[Double]):Double = {
;;     val total = values.foldLeft(0d)(_ + _)
;;     total / values.size
;;   }

;;   def standardDeviation(mean:Double, values:Array[Double]):Double = {
;;     val squares = values.map(v => (v - mean) * (v - mean))
;;     val sum = squares.foldLeft(0d)(_ + _)
;;     math.sqrt(sum / values.size)
;;   }
;; }

;; object MovieLens {

;;   def openFile(path:String):io.BufferedSource = {
;;     new io.BufferedSource(new java.io.FileInputStream(path))
;;   }

;;   def main(args:Array[String]) = {
;;     val set = new MovieLens(
;;       openFile(args(0)),
;;       openFile(args(1)),
;;       openFile(args(2)))

;;     val movies = set.movies.keySet
;;     var sims = Seq[Tuple2[Double,String]]()
;;     movies.foreach { m =>
;;       val sim = set.similarity(86, m)
;;       sims = sims :+ (sim, set.movies(m))
;;     }
;;     sims.sorted.foreach { i =>
;;       printf("%3f %s\n", i._1, i._2)
;;     }
;;   }
;; }
