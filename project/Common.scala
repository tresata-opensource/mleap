package ml.combust.mleap

import sbt._
import Keys._
import com.typesafe.sbt.SbtPgp.autoImportImpl._
import com.typesafe.sbt.pgp.PgpKeys._
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype.autoImport._

object Common {
  lazy val defaultMleapSettings = defaultSettings ++ mleapSettings ++ publishSettings
  lazy val defaultBundleSettings = defaultSettings ++ bundleSettings ++ publishSettings
  lazy val defaultMleapXgboostSparkSettings = defaultMleapSettings ++ publishSettings
  lazy val defaultMleapServingSettings = defaultMleapSettings ++ noPublishSettings

  lazy val defaultSettings = buildSettings ++ publishSettings

  lazy val buildSettings: Seq[Def.Setting[_]] = Seq(
    scalaVersion := "2.11.8",
    crossScalaVersions := Seq("2.11.8", "2.12.8"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
    fork in Test := true,
    javaOptions in test += sys.env.getOrElse("JVM_OPTS", ""),
    resolvers += Resolver.mavenLocal,
    resolvers ++= {
      // Only add Sonatype Snapshots if this version itself is a snapshot version
      if(version.value.endsWith("SNAPSHOT")) {
        Seq(
          "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
          "ASF Snapshots" at "https://repository.apache.org/content/groups/snapshots"
        )
      } else {
        Seq()
      }
    },
    resolvers += "bintray-tresata-maven" at "https://dl.bintray.com/tresata/maven"
  )

  lazy val mleapSettings: Seq[Def.Setting[_]] = Seq(organization := "ml.combust.mleap")
  lazy val bundleSettings: Seq[Def.Setting[_]] = Seq(organization := "ml.combust.bundle")

  lazy val noPublishSettings: Seq[Def.Setting[_]] = Seq(
    publishTo in publishSigned := None,
    publishTo := None
  )

  lazy val publishSettings: Seq[Def.Setting[_]] = Seq(
    //sonatypeProfileName := "ml.combust",
    //releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    publishMavenStyle := true,
    publishTo := Some({
      if (version.value.endsWith("SNAPSHOT")) {
        "oss-libs-snapshot-local"  at "http://server02.tresata.com:8081/artifactory/oss-libs-snapshot-local"
      } else {
        "oss-libs-release-local" at "http://server02.tresata.com:8081/artifactory/oss-libs-release-local"
      }
    }),
    credentials += Credentials(Path.userHome / ".m2" / "credentials_artifactory"),
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    licenses := Seq("Apache 2.0 License" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("https://github.com/combust/mleap")),
    scmInfo := Some(ScmInfo(url("https://github.com/combust/mleap.git"),
      "scm:git:git@github.com:combust/mleap.git")),
    developers := List(Developer("hollinwilkins",
      "Hollin Wilkins",
      "hollinrwilkins@gmail.com",
      url("http://hollinwilkins.com")),
      Developer("seme0021",
        "Mikhail Semeniuk",
        "mikhail@combust.ml",
        url("https://www.linkedin.com/in/semeniuk")),
      Developer("ancasarb",
        "Anca Sarb",
        "sarb.anca@gmail.com",
        url("https://www.linkedin.com/in/anca-sarb")))
  )
}
