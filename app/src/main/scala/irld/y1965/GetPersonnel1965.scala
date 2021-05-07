package irld.y1965

import better.files._

import scala.collection.mutable

object GetPersonnel1965 {
  val PersonnelRgx = """(?s)(?<=Research Personnel:)(.+?)(?=Recruiting Contact:|$)""".r
  val Year = "1965"
  val Dir = "./app/src/main/resources/y" + Year
  val UntilLnNum = 55383

  def main(args: Array[String]): Unit = {

    val lines = File(Dir, Year + "-no-empty-lines.txt").lines(DefaultCharset).toList
    val lineNumbers = File(Dir, Year + "-parent-line-numbers.tsv").lines(DefaultCharset).toList
    val withPersonnel = File(Dir, Year + "-parents-with-personnel.tsv")
    withPersonnel overwrite ""

    for (numIdx <- lineNumbers.indices) {
      val startLnIdx = lnIdxOfNumLine(lineNumbers(numIdx))
      val startLnNo = lnNoOfIdx(startLnIdx)
      println(s"$startLnNo ${lines(startLnIdx)}")
      val untilLnIdx = if (numIdx < lineNumbers.size - 1) lnIdxOfNumLine(lineNumbers(numIdx + 1)) else idxOfLnNo(UntilLnNum)
      println(s"lines: ${lnNoOfIdx(startLnIdx + 1)}-${lnNoOfIdx(untilLnIdx) - 1}")
      val linesAfterParent = lines.slice(startLnIdx + 1, untilLnIdx)
      val s = linesAfterParent.mkString(" ").replaceAll("\t", " ")

      val orgPersonnelList = mutable.ArrayBuffer[String]()

      PersonnelRgx.findAllMatchIn(s).toList.foreach { m =>
        val pers = m.group(1).trim
        println(s"'$pers' ${m.start}-${m.end}")
        orgPersonnelList += pers
      }
      withPersonnel.appendLine(s"$startLnNo\t${lines(startLnIdx)}\t${orgPersonnelList.mkString(" | ")}")
    }

    def lnIdxOfNumLine(numLine: String): Int = numLine.split("\t")(0).toInt - 1

    def idxOfLnNo(lnNo: Int): Int = lnNo - 1

    def lnNoOfIdx(idx: Int): Int = idx + 1
  }
}
