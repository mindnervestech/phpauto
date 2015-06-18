name := "autodealer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
"mysql" % "mysql-connector-java" % "5.1.18",
"net.coobird" % "thumbnailator" % "0.4.3",
javaJdbc,
  javaEbean,
  cache,
  "ws.securesocial" %% "securesocial" % "2.1.4"
)    

play.Project.playJavaSettings
