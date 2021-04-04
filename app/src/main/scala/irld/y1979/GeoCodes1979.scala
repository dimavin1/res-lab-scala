package irld.y1979

import better.files._

object GeoCodes1979 {

  def main(args: Array[String]): Unit = {
    val Year = "1979"
    val Dir = "./app/src/main/resources/y" + Year
    val DivisionRgx = "(?i)division".r
    val CompanyCodeRgx = """[A-Z].*?\b(([A-Z][0-9liI]+?)|([0-9l]{2,}))([,.][0-9lI]{0,3})?\b""".r
    val DecimalRgx = """[.,].{1,3}$""".r

    val in = File(Dir, Year + "-geo-parents-manual.txt")
    val geoDistinct = File(Dir, Year + "-geo-parents-distinct.txt")
    //    val compSet = mutable.TreeSet[String]()

    geoDistinct overwrite ""

    val lines = in.lines(DefaultCharset).toList

    lines.
      filter(DivisionRgx.unanchored.matches(_)).
      map { l => geoDistinct.appendLine(l) }
  }
}
