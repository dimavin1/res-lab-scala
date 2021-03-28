package irld.y1970

import better.files._

import scala.collection.mutable

object GeoParser1970 {
  val YEAR = "1970"
  private[y1970] val Dir = "./app/src/main/resources/y" + YEAR
  private[y1970] val CompanyRgx =
    "([A-Z][a-zA-Z&.,'!;()/ -]{3,})(?i)(,\\sInc.)?(?i)(,\\sIncorporated)?[,.]?\\s[A-Z]?[0-9 l]+(\\.\\d{1,3})?".r
  private[y1970] val SubRgx = ".*\\.\\d{1,3}.*".r

  def main(args: Array[String]): Unit = {
    val geoIndex = File(Dir, "1970_geo_index.txt")
    val companies = File(Dir, "1970_comp.txt")
    val parents = File(Dir, "1970_parents.txt")
    val distinctParents = File(Dir, "1970_distinct_parents.txt")
    val compSet = mutable.TreeSet[String]()

    companies overwrite ""
    parents overwrite ""
    distinctParents overwrite ""

    val lines = geoIndex lines DefaultCharset
    lines foreach {
      saveCompanies(companies, parents, compSet, _)
    }
    compSet foreach {
      distinctParents appendLine _
    }
  }

  def saveCompanies(companies: File, parents: File, compSet: mutable.TreeSet[String], s: String): Unit = {
    val ms = CompanyRgx.findAllMatchIn(s)
    for (m <- ms) {
      val company = m.group(0)
      println(company)
      companies.appendLine(company)
      if (!SubRgx.matches(company)) {
        parents.appendLine(company)
        compSet += company
      }
    }
  }
}
