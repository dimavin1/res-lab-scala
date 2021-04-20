package irld.y1982

import irld.y1982.GeoParser1982.{SpaceNotAfterPointRgx, improveCode}
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParserSpec extends AnyFunSpec {

  describe("Parser") {
    it("should set code letter") {
      val in = "Omnicomp, Inc 061"
      improveCode(in) shouldBe in
    }

    it("should match a space following not point") {
      SpaceNotAfterPointRgx.unanchored.matches("Omnicomp, Inc 061") shouldBe true
      SpaceNotAfterPointRgx.unanchored.matches("Harvey Hubbell Inc, Lighting Division H206. 6") shouldBe false

    }
  }
}