import sbt._

class BrainsProject(info: ProjectInfo) extends DefaultProject(info) with Exec
{
  val scalatest = "org.scalatest" % "scalatest" % "1.2"
  
  def doClassify(classname:String, args:Array[String]) = runTask(Some(classname), runClasspath, args) dependsOn(compile)

  lazy val classify = task { args =>
    (args.first, args.size) match {
      case ("dtree", 3) => doClassify("brains.classification.DecisionTree", args.drop(1))
      case ("bayes", 3) => doClassify("brains.classification.NaiveBayes", args.drop(1))
      case _ => task { Some("usage: classify dtree|bayes outcomekey dataset") }
    }
  } describedAs "run a classifier on the specified data set"

}
