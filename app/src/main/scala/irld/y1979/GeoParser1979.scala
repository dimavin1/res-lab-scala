package irld.y1979

import better.files._

object GeoParser1979 {

  def main(args: Array[String]): Unit = {
    val Year = "1979"
    val Dir = "./app/src/main/resources/y" + Year
    val AllCapsRgx = ".*[A-Z]{5,}$".r
    val CompanyCodeRgx = """[A-Z].*?\b(([A-Z][0-9liI]+?)|([0-9l]{2,}))([,.][0-9lI]{0,3})?\b""".r
    val DecimalRgx = """[.,].{1,3}$""".r

    val in = File(Dir, Year + "-no-empty-lines.txt")
    val geoLines = File(Dir, Year + "-geo-lines.txt")
    val noAllCaps = File(Dir, Year + "-geo-no-all-caps.txt")
    val oneLine = File(Dir, Year + "-geo-one-line.txt")
    val companies = File(Dir, Year + "-geo-comp.txt")
    val parents = File(Dir, Year + "-geo-parents.txt")
    //    val compSet = mutable.TreeSet[String]()

    geoLines overwrite ""
    noAllCaps overwrite ""
    oneLine overwrite ""
    companies overwrite ""
    parents overwrite ""

    val lines = in.lines(DefaultCharset).toList

    filterByLnNo(lines).
      map { case (s, _) => geoLines.appendLine(s) }
    filterByLnNo(lines).
      filter { case (s, _) => !AllCapsRgx.matches(s) }.
      map { case (s, _) => noAllCaps.appendLine(s) }
    val allCompLines = filterByLnNo(lines).
      filter { case (s, _) => !AllCapsRgx.matches(s) }.
      map(_._1).mkString(" ")
    oneLine.write(allCompLines)
    val ms = CompanyCodeRgx.findAllMatchIn(allCompLines)
    ms.foreach { m =>
      val c = m.group(0)
      companies.appendLine(c)
      if (!DecimalRgx.unanchored.matches(c)) parents.appendLine(c)
    }
  }

  private def filterByLnNo(lines: List[String]) = {
    lines.zipWithIndex.
      filter { case (_, i) => i + 1 >= 63376 && i + 1 <= 77563 }
  }
}
