import sbt._

class HelloWorldProject(info: ProjectInfo) extends DefaultProject(info)
{
  def droolsVer = "5.1.0.M1"
  val jbossRepo = "jboss maven repo" at "http://repository.jboss.org/maven2"
  val drools_api = "org.drools" % "drools-api" % droolsVer
  val drools_core = "org.drools" % "drools-core" % droolsVer
  val drools_compiler = "org.drools" % "drools-compiler" % droolsVer
  val scalatest = "org.scalatest" % "scalatest" % "1.2"
}
