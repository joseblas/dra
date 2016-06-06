name := "basic-draper"

organization := "con.sky.sns.innovation"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.5"
//scalaVersion := "2.10.4"

//crossScalaVersions := Seq("2.10.4", "2.11.2")

//scalacOptions += "-target:jvm-1.7"

resolvers ++= Seq(
  //"WH Nexus Repository" at "http://maven.apps.local:8082/nexus/content/groups/public",
  "public " at "http://www.sparkjava.com/nexus/content/repositories/spark/",
  "artifactory" at "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"
)

val sparkVersion = "1.6.0"
val hazelcastVersion = "3.5.4"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion, // % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion, // % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion, // % "provided",
  "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion, // % "provided",
  "com.hazelcast" % "hazelcast-client" % hazelcastVersion,
  "com.sky.sns" %% "magpie-api" % "2.0.0-SNAPSHOT",
  "com.datastax.spark" %% "spark-cassandra-connector" % "1.5.0-M3",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.slf4j" % "slf4j-log4j12" % "1.7.12",
  "org.scalaz" %% "scalaz-core" % "7.1.2",
  "com.github.scopt" %% "scopt" % "3.3.0",
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "com.typesafe.scala-logging" %% "scala-logging-api" % "2.1.2",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
)

//initialCommands := "import example._"

unmanagedClasspath in Runtime += baseDirectory.value / "config"

//spName := "organization/configmy-awesome-spark-package"


mainClass in assembly := Some("com.sky.sns.innovation.app.HeatMap")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF","MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF","DUMMY.DSA") => MergeStrategy.discard
  case PathList("META-INF","DUMMY.SF") => MergeStrategy.discard
  case PathList("META-INF","ECLIPSEF.SF") => MergeStrategy.discard
  case PathList("META-INF","ECLIPSEF.RSA") => MergeStrategy.discard
  case _ => MergeStrategy.last
}
