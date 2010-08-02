#!/usr/bin/env scala
!#

import collection.Set
import collection.{mutable => mut}

abstract class SimilarityModel {
  val ratings = mut.Map[String,mut.Map[String,Int]]()
  def addRating(user:String, item:String, rating:Int)
  def rating(key:String, value:String):Option[Int] = {
    if (ratings.contains(key))
      ratings(key).get(value)
    else
      None
  }
  def items:Set[String]
  def users:Set[String]
}

class UserSimilarityModel extends SimilarityModel {
  def addRating(user:String, item:String, rating:Int) = {
    ratings.getOrElseUpdate(user, mut.Map[String,Int]())
    ratings(user)(item) = rating
  }
  def items:Set[String] = ratings.values.flatMap(_.keySet).toSet
  def users:Set[String] = ratings.keySet
}

class ItemSimilarityModel extends SimilarityModel {
  def addRating(user:String, item:String, rating:Int) = {
    ratings.getOrElseUpdate(item, mut.Map[String,Int]())
    ratings(item)(user) = rating
  }
  def items:Set[String] = ratings.keySet
  def users:Set[String] = ratings.values.flatMap(_.keySet).toSet
}

class RatingSet(val data:Seq[Seq[String]], val model:SimilarityModel) {

  val totals = mut.Map[String,Int]()
  for (r <- data) {
    var user = r(0)
    var item = r(1)
    var rating = r(2)
    model.addRating(user, item, rating.toInt)
    totals.getOrElseUpdate(user, 0)
    totals(user) += 1
  }

  lazy val users = model.users.toSeq.sorted
  lazy val items = model.items.toSeq.sorted

  def rating(a:String, b:String) = model.rating(a, b)

  // calculate euclidiean distance similarity
  def esimilarity(a:String, b:String):Double = {
    var common = 0
    var sim = 0.0d
    var max_common = math.min(model.ratings(a).keys.size, model.ratings(b).keys.size)
    for ((item, rating) <- model.ratings(a)) {
      model.rating(b, item) match {
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

def recommendItems(user:String, ratings:RatingSet) = {
  var items = ratings.items.toSeq.sorted
  var first = items.head
  var rest = items.tail
  rest.map(i => (i, ratings.esimilarity(first, i))).sortBy(_._2).reverse
}

def estimateUserBasedRating(user:String, item:String, ratings:RatingSet):Double = {
  var estimate = Double.NaN
  var simSum = 0.0
  var weightSum = 0.0
  val userRating = ratings.rating(user, item)
  if (userRating.nonEmpty) {
    return userRating.get.toDouble
  }
  for (other <- ratings.users) {
    val otherRating = ratings.rating(other, item)
    if (otherRating.nonEmpty) {
      val sim = ratings.esimilarity(user, other)
      val weight = sim * otherRating.get
      weightSum += weight
      simSum += sim
    }
  }
  if (simSum > 0.0)
    weightSum / simSum
  else
    Double.NaN
}

val lines = io.Source.stdin.getLines.toSeq
val data = lines.map(_.trim.split(":").toSeq)

// calc item-based rec
val itemSet = new RatingSet(data, new ItemSimilarityModel)
val user = itemSet.users.head
val recs = recommendItems(user, itemSet)
println("item-based recs for " + user)
for (rec <- recs) {
  println(rec)
}

// calc user-based sim
val userSet = new RatingSet(data, new UserSimilarityModel)
val users = userSet.users.toSeq.sorted
val subject = users.head
val others = users.tail
for (o <- others) {
  val es = userSet.esimilarity(subject, o)
  printf("%s %s esim %.4f\n", subject, o, es)
}

// estimate ratings
for (item <- itemSet.items) {
  val estimate = estimateUserBasedRating(user, item, userSet)
  println("estimated rating for %s of %s is %f".format(user, item, estimate))
}

