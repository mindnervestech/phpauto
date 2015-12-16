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
"com.google.apis" % "google-api-services-calendar" % "v3-rev87-1.19.0",
"com.google.apis" % "google-api-services-tasks" % "v1-rev41-1.21.0",
"com.google.apis" % "google-api-services-oauth2" % "v2-rev75-1.19.0",
"com.google.http-client" % "google-http-client-jackson2" % "1.19.0",
"com.google.oauth-client" % "google-oauth-client-jetty" % "1.19.0",
"com.google.oauth-client" % "google-oauth-client" % "1.19.0",
"org.apache.velocity" % "velocity-tools" % "2.0",
javaJdbc,
  javaEbean,
  cache,
  filters,
  "ws.securesocial" %% "securesocial" % "2.1.4",
  "com.itextpdf" % "itextpdf" % "5.3.2"
)    

play.Project.playJavaSettings
