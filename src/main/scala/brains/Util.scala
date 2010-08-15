package brains

import collection.mutable.{ Map => MMap }

class MutableMapGetOrElseUpdateWrapper[A,B](m: MMap[A,B]) {
  def +?(k:A, v:B) = m.getOrElseUpdate(k, v)
}

object util {
  implicit def map2MutWrapper[A,B](m:MMap[A,B]) = new MutableMapGetOrElseUpdateWrapper(m)
}
