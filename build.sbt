name := "autodealer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
"mysql" % "mysql-connector-java" % "5.1.18",
"net.coobird" % "thumbnailator" % "0.4.3",
"org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.5",
"net.sf.opencsv" % "opencsv" % "2.3",
"commons-net" % "commons-net" % "3.2",
"org.apache.velocity" % "velocity" % "1.7",
"org.apache.velocity" % "velocity-tools" % "2.0",
javaJdbc,
  javaEbean,
  cache,
  "ws.securesocial" %% "securesocial" % "2.1.4"
)    

play.Project.playJavaSettings
