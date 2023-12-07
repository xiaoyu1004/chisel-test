import $ivy.`com.goyeau::mill-scalafix_mill0.10:0.3.1`
import com.goyeau.mill.scalafix.ScalafixModule
import mill._, scalalib._, scalafmt._

import mill.scalalib.TestModule.ScalaTest

object ChiselTest extends SbtModule with ScalafixModule with ScalafmtModule { m =>
  override def millSourcePath = os.pwd
  override def scalaVersion = "2.13.10"
  override def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
  )
  override def ivyDeps = Agg(
    ivy"edu.berkeley.cs::chisel3:3.6.0",
  )
  override def scalacPluginIvyDeps = Agg(
    ivy"edu.berkeley.cs:::chisel3-plugin:3.6.0",
  )

  object test extends Tests with ScalaTest {
    override def ivyDeps = m.ivyDeps() ++ Agg(
      ivy"edu.berkeley.cs::chiseltest:0.6.0"
    )
  }
}