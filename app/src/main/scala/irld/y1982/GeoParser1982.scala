package irld.y1982

import better.files._

import scala.collection.mutable

object GeoParser1982 {
  val Year = "1982"
  val StartLine = 57640
  val EndLine = 70929
  val CodeInsideLineRgx = """\d+( )(?=.{4,})""".r
  val SpaceAfterPointRgx = """(?<=\.) (?=\d{1,3}$)"""
  val SpaceNotAfterPointRgx = """(?<=[^.]) (?=\d{1,3}$)""".r

  def main(args: Array[String]): Unit = {
    val Dir = "./app/src/main/resources/y" + Year
    val AllCapsRgx = "^[A-Z]+( [A-Z]+)*$".r
    val LeftPageHdrRgx = """^\d\d\d [A-Z]+( [A-Z]+)*$""".r
    val RightPageHdrRgx = """^[A-Z]+( [A-Z]+)* \d\d\d$""".r
    val CompanyCodeRgx = """[A-Z].*?\b(([A-Z][0-9liI]+?)|([0-9l]{2,}))([,.][0-9lI]{0,3})?\b""".r
    val DecimalRgx = """[.,].{1,3}$""".r

    val in = File(Dir, Year + "-no-empty-lines.txt")
    val geoLines = File(Dir, Year + "-geo-lines.txt")
    val noAllCaps = File(Dir, Year + "-geo-no-all-caps.txt")
    val linesSplit = File(Dir, Year + "-geo-split-lines.txt")
    val oneLine = File(Dir, Year + "-geo-one-line.txt")
    val companies = File(Dir, Year + "-geo-comp.txt")
    val parents = File(Dir, Year + "-geo-parents.txt")
    val splitLineList = mutable.ArrayBuffer[String]()

    geoLines overwrite ""
    noAllCaps overwrite ""
    linesSplit overwrite ""
    oneLine overwrite ""
    companies overwrite ""
    parents overwrite ""

    val lines = filterByLnNo(in.lines(DefaultCharset).toList)

    lines.
      map { geoLines.appendLine }

    val linesNoAllCaps = lines.
      filter { s => !AllCapsRgx.matches(s) && !LeftPageHdrRgx.matches(s) && !RightPageHdrRgx.matches(s) }
    linesNoAllCaps.
      map { noAllCaps.appendLine }

    linesNoAllCaps.foreach { l =>
      var compStart = 0
      val allMatches = GeoParser1982.CodeInsideLineRgx.findAllMatchIn(l).toList
      allMatches.foreach { m =>
        println(s"'${m.group(1)}' '${m.start}-${m.end}' '${l.substring(compStart, m.end - 1)}'")
        splitLineList += l.substring(compStart, m.end - 1)
        compStart = m.end
      }
      println(s"'${l.substring(compStart)}'")
      splitLineList += l.substring(compStart).replaceAll(SpaceAfterPointRgx, "")
    }
    splitLineList.map { linesSplit.appendLine }

    val allCompLines = linesNoAllCaps.
      mkString(" ")
    oneLine.write(allCompLines)
    val ms = CompanyCodeRgx.findAllMatchIn(allCompLines)
    ms.foreach { m =>
      val c = m.group(0).trim
      companies.appendLine(c)
      if (!DecimalRgx.unanchored.matches(c)) parents.appendLine(c)
    }
  }

  private def filterByLnNo(lines: List[String]) = {
    lines.zipWithIndex.
      filter { case (_, i) => i + 1 >= StartLine && i + 1 <= EndLine }.map { case (s, _) => s }
  }
}
