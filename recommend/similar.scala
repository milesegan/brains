#!/usr/bin/env scala
!#

import collection.mutable

abstract class SimilarityModel {
  val ratings = mutable.Map[String,mutable.Map[String,Int]]()
  def addRating(user:String, item:String, rating:Int)
  def rating(key:String, value:String):Option[Int] = {
    if (ratings.contains(key))
      ratings(key).get(value)
    else
      None
  }
}

class UserSimilarityModel extends SimilarityModel {
  def addRating(user:String, item:String, rating:Int) = {
    ratings.getOrElseUpdate(user, mutable.Map[String,Int]())
    ratings(user)(item) = rating
  }
}

class ItemSimilarityModel extends SimilarityModel {
  def addRating(user:String, item:String, rating:Int) = {
    ratings.getOrElseUpdate(item, mutable.Map[String,Int]())
    ratings(item)(user) = rating
  }
}

class RatingSet(val lines:Seq[String], val model:SimilarityModel) {

  val data = lines.map(_.trim.split(":"))
  val totals = mutable.Map[String,Int]()

  for (r <- data) {
    var rater = r(0)
    var thing = r(1)
    var rating = r(2)
    model.addRating(rater, thing, rating.toInt)
    totals.getOrElseUpdate(rater, 0)
    totals(rater) += 1
  }

  def raters = totals.keySet
  def things = model.ratings.keySet

  // calculate simple jaccard similarity
  def jsimilarity(a:String, b:String):Double = {
    var total = 0
    var similar = 0f
    for ((thing, rating) <- model.ratings(a)) {
      model.rating(b, thing) match {
        case e:Some[_] => {
          total += 1
          if (e.get == rating) {
            similar += 1
          }
        }
        case _ => ()
      }
    }
    similar.toDouble / total.toDouble
  }

  // calculate euclidiean distance similarity
  def esimilarity(a:String, b:String):Double = {
    var common = 0
    var sim = 0.0d
    var max_common = math.min(model.ratings(a).keys.size, model.ratings(b).keys.size)
    for ((thing, rating) <- model.ratings(a)) {
      model.rating(b, thing) match {
        case e:Some[_] => {
          common += 1
          sim += math.pow(rating - e.get, 2)
        }
        case _ => ()
      }
    }
    if (common > 0) {
      sim = math.sqrt(sim / common)
      sim = 1.0 - math.tanh(sim)
      sim * common / max_common
    }
    else {
      0
    }
  }
}

val lines = io.Source.stdin.getLines.toSeq

// calc user-based sim
val userSet = new RatingSet(lines, new UserSimilarityModel)
val raters = userSet.raters.toSeq.sorted
val subject = raters.head
val others = raters.tail
for (o <- others) {
  val js = userSet.jsimilarity(subject, o)
  val es = userSet.esimilarity(subject, o)
  printf("%s %s jsim %.4f\n", subject, o, js)
  printf("%s %s esim %.4f\n", subject, o, es)
}

// calc item-based sim
val itemSet = new RatingSet(lines, new UserSimilarityModel)
var things = itemSet.things.toSeq.sorted
var first = things.head
var rest = things.tail
val esims = rest.map(i => (i, itemSet.esimilarity(first, i))).sortBy(_._2).reverse
println("item-based recs for " + first)
for (sim <- esims) {
  println(sim)
}
