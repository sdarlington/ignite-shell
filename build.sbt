name := "ignite-shell"

version := "0.1"

scalaVersion := "2.12.8"

val productName = "org.gridgain"
val igniteVersion = "8.7.6"

resolvers += "GridGain External Repository" at "https://www.gridgainsystems.com/nexus/content/repositories/external"

libraryDependencies += productName % "ignite-core" % igniteVersion
libraryDependencies += productName % "ignite-indexing" % igniteVersion
libraryDependencies += productName % "ignite-ml" % igniteVersion
libraryDependencies += productName % "ignite-scalar" % igniteVersion
libraryDependencies += productName % "gridgain-core" % igniteVersion

libraryDependencies += "com.lihaoyi" % "ammonite" % "1.7.1" cross CrossVersion.full
