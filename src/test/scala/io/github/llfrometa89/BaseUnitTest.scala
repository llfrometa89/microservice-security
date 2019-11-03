package io.github.llfrometa89

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

trait BaseUnitTest extends AnyFunSpec with Matchers with BeforeAndAfter with ScalaFutures
