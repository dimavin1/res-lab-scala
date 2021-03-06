package irld.y1960

import better.files._

import scala.collection.mutable

object GetPersonnel1960 {
  private val PersonnelRgx = """(?s)(?<=Research staff:)(.+?)(?=Research on:|$)""".r
  private val Year = "1960"
  private val Dir = "./app/src/main/resources/y" + Year
  private val UntilLnNum = 53880

  def main(args: Array[String]): Unit = {
    val lines = File(Dir, Year + ".txt").lines(DefaultCharset).toList
    val lineNumbers = File(Dir, Year + "-line-index-parent.tsv").lines(DefaultCharset).toList
    val withPersonnel = File(Dir, Year + "-parents-with-personnel.tsv")
    withPersonnel overwrite ""

    for (numIdx <- lineNumbers.indices) {
      val startLnIdx = lnIdxOfNumLine(lineNumbers(numIdx))

      val startLnNo = lnNoOfIdx(startLnIdx)
      val lineNumber = lineNumbers(numIdx).split("\t")
      println(s"$startLnNo ${lines(startLnIdx)}")
      val untilLnIdx = if (numIdx < lineNumbers.size - 1) lnIdxOfNumLine(lineNumbers(numIdx + 1)) else idxOfLnNo(UntilLnNum)
      println(s"lines: ${lnNoOfIdx(startLnIdx + 1)}-${lnNoOfIdx(untilLnIdx) - 1}")
      val linesAfterParent = lines.slice(startLnIdx + 1, untilLnIdx)
      val s = linesAfterParent.mkString(" ").replaceAll("\t", " ").replaceAll(" +", " ")

      val orgPersonnelList = mutable.ArrayBuffer[String]()

      PersonnelRgx.findAllMatchIn(s).toList.foreach { m =>
        val pers = m.group(1).trim
        println(s"'$pers' ${m.start}-${m.end}")
        orgPersonnelList += pers
      }
      withPersonnel.appendLine(s"$startLnNo\t${lineNumber(1)}\t${lineNumber(2)}\t${orgPersonnelList.mkString(" | ")}")
    }

    def lnIdxOfNumLine(numLine: String): Int = numLine.split("\t")(0).toInt - 1

    def idxOfLnNo(lnNo: Int): Int = lnNo - 1

    def lnNoOfIdx(idx: Int): Int = idx + 1
  }
}
