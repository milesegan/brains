#!/usr/bin/env scala
!#

import collection.mutable

sealed trait Mode
case object UserMode extends Mode
case object ThingMode extends Mode

class RatingSet(val lines:Seq[String]) {

  val data = lines.map(_.trim.split(":"))
  val userRatings = mutable.Map[String,mutable.Map[String,Int]]()
  val thingRatings = mutable.Map[String,mutable.Map[String,Int]]()
  val totals = mutable.Map[String,Int]()

  for (r <- data) {
    var rater = r(0)
    var thing = r(1)
    var rating = r(2)
    userRatings.getOrElseUpdate(rater, mutable.Map[String,Int]())
    userRatings(rater)(thing) = rating.toInt
    thingRatings.getOrElseUpdate(thing, mutable.Map[String,Int]())
    thingRatings(thing)(rater) = rating.toInt
    totals.getOrElseUpdate(rater, 0)
    totals(rater) += 1
  }

  def raters = totals.keySet
  def things = thingRatings.keySet

  // calculate simple jaccard similarity
  def jsimilarity(a:String, b:String, mode:Mode):Double = {
    var total = 0
    var similar = 0f
    val ratings = ratingHash(mode)
    for ((thing, rating) <- ratings(a)) {
      ratings(b).get(thing) match {
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
  def esimilarity(a:String, b:String, mode:Mode):Double = {
    var common = 0
    var sim = 0.0d
    val ratings = ratingHash(mode)
    var max_common = math.min(ratings(a).keys.size, ratings(b).keys.size)
    for ((thing, rating) <- ratings(a)) {
      ratings(b).get(thing) match {
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

  private 
  def ratingHash(mode:Mode) = {
    mode match {
      case UserMode => userRatings
      case ThingMode => thingRatings
    }
  }

}

var d = new RatingSet(io.Source.stdin.getLines.toSeq)
val raters = d.raters.toSeq.sorted
val subject = raters.head
val others = raters.tail
for (o <- others) {
  val js = d.jsimilarity(subject, o, UserMode)
  val es = d.esimilarity(subject, o, UserMode)
  printf("%s %s jsim %.4f\n", subject, o, js)
  printf("%s %s esim %.4f\n", subject, o, es)
}
