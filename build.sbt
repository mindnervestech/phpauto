name := "autodealer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaEbean,
  cache,
  "ws.securesocial" %% "securesocial" % "2.1.4"
)    

play.Project.playJavaSettings
