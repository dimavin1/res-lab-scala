package irld.y1982

import better.files._

import scala.collection.mutable

object GeoFixCodes1982 {
  private[y1982] val CompanyCodeRgx = """(.*) (.{1,6})$""".r

  def main(args: Array[String]): Unit = {
    val Year = "1982"
    val Dir = "./app/src/main/resources/y" + Year
    val CodeRgx = " .{1,6}$".r

    val in = File(Dir, Year + "-geo-parents-manual.txt")
    val codeFixed = File(Dir, Year + "-geo-parents-code-fixed.txt")
    val parentsSorted = File(Dir, Year + "-geo-parents-sorted.tsv")
//    val parentsDistinct = File(Dir, Year + "-geo-parents-distinct.txt")
    val compList = mutable.ArrayBuffer[String]()
    val compSet = mutable.TreeSet[String]()

    codeFixed overwrite ""
    parentsSorted overwrite ""
//    parentsDistinct overwrite ""

    val lines = in.lines(DefaultCharset).toList

    lines.
      filter(CodeRgx.unanchored.matches(_)).
      map {
        case CompanyCodeRgx(company, code) =>
          val fixed = fixCode(company, code)
          codeFixed.appendLine(s"$company $fixed")
          compList += s"$company $fixed"
          compSet.add(s"$company $fixed")
      }
//    compList.sortWith(byCode).map {
//      case CompanyCodeRgx(company, code) =>
//        parentsSorted.appendLine(s"${padZeros(code)}\t$company")
//    }
  }

  def byCode(l1: String, l2: String): Boolean = {
    padZeros(getCode(l1)) < padZeros(getCode(l2))
  }

  def padZeros(code: String): String = {
    val s = f"${code(0)}${code.substring(1).toInt}%03d"
    s
  }

  def getCode(line: String): String = line match {
    case CompanyCodeRgx(_, code) => code
  }

  def fixCode(company: String, code: String): String = {
    val c0 = code(0) match {
      case l if l.isLetter => code(0)
      case '0' => 'O'
      case '1' => 'I'
      case _ => '#'
    }
    val c = if (!codeStartsWithCompanyWords(c0, company)) "#" + c0 else c0
    val number = code.substring(1).map(fixDigit)
    c + number
  }

  def codeStartsWithCompanyWords(c0: Char, company: String): Boolean = company.split(' ').map(_ (0)).contains(c0)

  def fixDigit(c: Char): Char = c match {
    case d if d.isDigit => d
    case 'l' | 'i' | 'I' => '1'
    case 'O' => '0'
    case _ => '#'
  }
}
