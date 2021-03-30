package irld.y1979

import better.files._

object DirParser1979 {
  val Year = "1979"
  private[y1979] val Dir = "./app/src/main/resources/y" + Year
  private[y1979] val CompanyRgx =
    """(?<!-)(([A-Z][A-Z&.'!;()/ -]{3,})(?i)(,\sInc\.?)?(?i)(,\sIncorporated)?)""".r
  private[y1979] val SubRgx = """.*\.\d{1,3}.*""".r

  def main(args: Array[String]): Unit = {
    val in = File(Dir, Year + "-no-empty-lines.txt")
    val parents = File(Dir, Year + "-parents.txt")
    parents overwrite ""

    val lines = in lines DefaultCharset
    lines.zipWithIndex.
      filter { case (_, i) => i > 89 && i < 63359 }.
      filter { case (s, _) => """\b(?i)(See)\b""".r.findFirstIn(s).isEmpty }.
      filter { case (s, _) => s(0) != '-' }.
      filter { case (s, _) => CompanyRgx.findFirstMatchIn(s).isDefined && CompanyRgx.findFirstMatchIn(s).get.start < 3 }.
      map { case (s, i) => parents.appendLine(s"${i + 1}: ${CompanyRgx.findFirstIn(s).get}") }
  }
}
