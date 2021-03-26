package irld.y1970

import better.files._

object GeoParser1970 {
  val YEAR = "1970"
  private[y1970] val Dir = "./app/src/main/resources/y" + YEAR
  private[y1970] val CompanyRgx =
    "([A-Z][a-zA-Z&.,'!;()/ -]{3,})(?i)(,\\sInc.)?(?i)(,\\sIncorporated)?[,.]?\\s[A-Z]?[0-9 l]+(\\.\\d{1,3})?".r

  @throws[Exception]
  def main(args: Array[String]): Unit = try {
    val inFile: File = File(Dir, "1970_geo_index.txt")
    val lines = inFile lines DefaultCharset
    lines foreach {
      printCompanies
    }
  }

  def printCompanies(s: String): Unit = {
    val ms = CompanyRgx.findAllMatchIn(s)
    for (m <- ms)
      println(m.group(0))
  }
}
