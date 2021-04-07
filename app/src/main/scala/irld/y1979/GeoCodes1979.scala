package irld.y1979

import better.files._

object GeoCodes1979 {

  def main(args: Array[String]): Unit = {
    val Year = "1979"
    val Dir = "./app/src/main/resources/y" + Year
    val CodeRgx = " .{1,6}$".r
    val CompanyCodeRgx = """(.*) (.{1,6})$""".r

    val in = File(Dir, Year + "-geo-parents-manual.txt")
    val codeFixed = File(Dir, Year + "-geo-parents-code-fixed.txt")

    codeFixed overwrite ""

    val lines = in.lines(DefaultCharset).toList

    lines.
      filter(CodeRgx.unanchored.matches(_)).
      map {
        case CompanyCodeRgx(company, code) =>
          val fixed = fixCode(company, code)
          codeFixed.appendLine(s"$company $fixed")
      }
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
