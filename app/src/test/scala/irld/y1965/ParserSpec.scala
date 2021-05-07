package irld.y1965

import irld.y1965.GetPersonnel1965.PersonnelRgx
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParserSpec extends AnyFunSpec {

  describe("Regex") {
    it("should select text") {
      val s =
        """Research Personnel:
          |W.	E. Lowrey, Dir. Engrg. & Research (Mechanical)
          |M. T. Works, Mgr. Dev. Engrg. (Mechanical)
          |R. M. Estes, Mgr. Product Engineering (Mechanical) M. R. Chance, Mgr. Metallurgy
          |Laboratory Staff: Chemists 1, mech. engr. 7, chem. engr. 1, industrial engr. 1, math. 1, metallurgists 2, techn. 10, other lab staff 7.
          |Recruiting Contact: W. E. Lowrey, Dir. Engrg. & Research""".stripMargin
      val PersonnelRgx = """(?s)(?<=Research Personnel:)(.+?)(?=Recruiting Contact:)""".r
      val list = PersonnelRgx.findAllMatchIn(s).toList
      list.foreach{ m =>
        println(s"'${m.group(1).replaceAll("\n", " ").trim}' ${m.start}-${m.end}")
      }

    }

  }
}