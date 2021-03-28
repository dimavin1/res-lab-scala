package irld.y1970

import better.files._

import scala.collection.mutable
import scala.collection.mutable.TreeSet

object GeoParser1970 {
  val YEAR = "1970"
  private[y1970] val Dir = "./app/src/main/resources/y" + YEAR
  private[y1970] val CompanyRgx =
    "([A-Z][a-zA-Z&.,'!;()/ -]{3,})(?i)(,\\sInc.)?(?i)(,\\sIncorporated)?[,.]?\\s[A-Z]?[0-9 l]+(\\.\\d{1,3})?".r
  private[y1970] val SubRgx = ".*\\.\\d{1,3}.*".r

  @throws[Exception]
  def main(args: Array[String]): Unit = try {
    val inFile: File = File(Dir, "1970_geo_index.txt")
    val outFile: File = File(Dir, "1970_comp.txt")
    val outFile2: File = File(Dir, "1970_parents.txt")
    val outFile3: File = File(Dir, "1970_distinct_parents.txt")
    val compSet: mutable.TreeSet[String] = mutable.TreeSet[String]()

    outFile.overwrite("")
    outFile2.overwrite("")
    outFile3.overwrite("")
    val lines = inFile lines DefaultCharset
    lines foreach {
      printCompanies(outFile, outFile2, compSet, _)
    }
    compSet.foreach {
      outFile3.appendLine().append(_)
    }
  }

  def printCompanies(outFile: File, outFile2: File, compSet: mutable.TreeSet[String], s: String): Unit = {
    val ms = CompanyRgx.findAllMatchIn(s)
    for (m <- ms) {
      val company = m.group(0)
      println(company)
      outFile.appendLine().append(company)
      if (!SubRgx.matches(company)) {
        outFile2.appendLine().append(company)
        compSet += company
      }
    }
  }
}
