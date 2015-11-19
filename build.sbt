name := "autodealer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
"mysql" % "mysql-connector-java" % "5.1.18",
"net.coobird" % "thumbnailator" % "0.4.3",
"org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.5",
"net.sf.opencsv" % "opencsv" % "2.3",
"com.opencsv" % "opencsv" % "3.5",
"commons-net" % "commons-net" % "3.2",
"org.apache.velocity" % "velocity" % "1.7",
"joda-time" % "joda-time" % "2.8.1",
"org.apache.velocity" % "velocity-tools" % "2.0",
javaJdbc,
  javaEbean,
  cache,
  filters,
  "ws.securesocial" %% "securesocial" % "2.1.4",
  "com.itextpdf" % "itextpdf" % "5.3.2"
)    

play.Project.playJavaSettings
