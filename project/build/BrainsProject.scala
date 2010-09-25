import sbt._
import Process._

class BrainsProject(info: ProjectInfo) extends DefaultProject(info) with Exec
{
  override def pomPath = "pom.xml"

  val apacheRepo = "apache" at "http://repository.apache.org"
  val scalalaNLPRepo = "scalanlp" at "http://repo.scalanlp.org/repo"
  // we need this for deps for scalala
  val rothamRepo = "rothamstead" at "http://ondex.rothamsted.bbsrc.ac.uk/nexus/content/groups/public/"

  val scalatest = "org.scalatest" % "scalatest" % "1.2"
  val scalanlp = "org.scalanlp" % "scalala_2.8.0" % "0.4.1-SNAPSHOT"
  
  def doClassify(classname:String, args:Array[String]) = runTask(Some(classname), runClasspath, args) dependsOn(compile)

  lazy val classify = task { args =>
    (args.first, args.size) match {
      case ("dtree", 3) => doClassify("brains.classification.DecisionTree", args.drop(1))
      case ("bayes", 3) => doClassify("brains.classification.NaiveBayes", args.drop(1))
      case _ => task { Some("usage: classify dtree|bayes outcomekey dataset") }
    }
  } describedAs "run a classifier on the specified data set"

  lazy val updateDoc = task {
    "rsync -av --delete " + docPath + "/main/api/ burgerkone.com:burgerkone.com/brains/" ! log
    None
  } describedAs("update published scaladoc for project")

}
