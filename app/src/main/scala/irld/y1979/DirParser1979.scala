package irld.y1979

import better.files._

import scala.collection.mutable

object DirParser1979 {
  val Year = "1979"
  private[y1979] val Dir = "./app/src/main/resources/y" + Year
  private[y1979] val CompanyRgx =
    """(?<!-)(([A-Z][A-Z&.'!;()/ -]{3,})(?i)(,\sInc\.?)?(?i)(,\sIncorporated)?)""".r
  private[y1979] val SubRgx = """.*\.\d{1,3}.*""".r

  def main(args: Array[String]): Unit = {
    val seeSet = mutable.TreeSet[Int]()
    val comp2ln = mutable.LinkedHashMap[String, Int]()

    val in = File(Dir, Year + "-no-empty-lines.txt")
    val parents = File(Dir, Year + "-parents.txt")
    parents overwrite ""
    val distParents = File(Dir, Year + "-distinct-parents.txt")
    distParents overwrite ""

    val lines = in lines DefaultCharset
    lines.zipWithIndex.
      filter { case (_, i) => i > 89 && i < 63359 }.
      filter { case (s, _) => """\b(?i)(See)\b""".r.findFirstIn(s).isDefined }.
      map { case (_, i) => seeSet.add(i) }

    lines.zipWithIndex.
      filter { case (_, i) => i > 89 && i < 63359 }.
      filter { case (s, _) => """\b(?i)(See)\b""".r.findFirstIn(s).isEmpty }.
      filter { case (_, i) => !seeSet.contains(i + 1) }. // the following line does not contain "See ..."
      filter { case (s, _) => s(0) != '-' }.
      filter { case (s, _) => CompanyRgx.findFirstMatchIn(s).isDefined && CompanyRgx.findFirstMatchIn(s).get.start < 3 }.
      map { case (s, i) =>
        val comp = "\t".r.replaceAllIn(CompanyRgx.findFirstIn(s).get, "")
        parents.appendLine(s"${i + 1}\t$comp")
        if(!comp2ln.keySet.contains(comp)) comp2ln.put(comp, i + 1)
      }
    comp2ln.keySet.foreach { c =>
      distParents.appendLine(s"${comp2ln(c)}\t$c")
    }
  }
}
