import sbt._
import Keys._
import sbtassembly.Plugin._
//import AssemblyKeys._ // put this at the top of the file

import sbtassembly.Plugin.AssemblyKeys
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin.assemblySettings
import sbtassembly.Plugin.MergeStrategy

name := "mia-scala-examples"

version := "1.0"

scalaVersion := "2.9.2"

//assemblySettings
seq(assemblySettings: _*)

resolvers ++= Seq(
    "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Sonatype Repository" at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
    "org.apache.hadoop" % "hadoop-core" % "1.0.4",
    "org.apache.mahout" % "mahout-core" % "0.7"
)


//mergeStrategy in assembly := MergeStrategy.first

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case "plugin.xml" =>
      MergeStrategy.first
    case x if x startsWith "org/apache/jasper" =>
      MergeStrategy.last
    case x if x startsWith "javax/xml" =>
      MergeStrategy.last
    case x if x startsWith "javax/servlet" =>
      MergeStrategy.last
    case x if x startsWith "org/apache/commons" =>
      MergeStrategy.last
    case x if x startsWith "org/apache/xmlcommons" =>
      MergeStrategy.last
    case x if x startsWith "org/xml/sax" =>
      MergeStrategy.last
    case x if x startsWith "org/w3c/dom" =>
      MergeStrategy.last
    case x => old(x)
  }
}


//libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test"

