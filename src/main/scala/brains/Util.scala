package brains

import collection.mutable.{ Map => MMap }

class MutableMapGetOrElseUpdateWrapper[T,U](m:MMap[T,U]) {
  def +?(k:T, v:U) = m.getOrElseUpdate(k, v)
}

object util {
  implicit def map2MutWrapper[T,U](m:MMap[T,U]) = new MutableMapGetOrElseUpdateWrapper(m)
}
