val swing = "org.scala-lang" % "scala-swing" % "2.10+"

lazy val root = (project in file(".")).
  settings(
    name := "Boulder Bearers",
    version := "1.0",
    scalaVersion := "2.10.4",
    scalaSource in Compile := baseDirectory.value / "src",
    mainClass in (Compile, run) := Some("boulderBearers.app.GameApp"),
	libraryDependencies += swing
  )
