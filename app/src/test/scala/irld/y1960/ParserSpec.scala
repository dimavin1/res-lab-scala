package irld.y1960

import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParserSpec extends AnyFunSpec with Matchers {

  describe("Regex") {
    it("should replace NBSP") {
      val s = "Director,  Research"
      val replaced = s.replaceAll(" +", " ")
      replaced should equal ("Director, Research")
    }
  }
}