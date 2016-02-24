package com.firelogs.scalalirc

import scala.collection.mutable.{Queue => MutableQueue, Map => MutableMap}
import scala.io.Source

import sys.process._

import java.io.{FileNotFoundException, IOException}

class ScalaLirc(sourceFile: String = "/etc/lirc/lircd.conf") {
  
  val auxRemoteCodes: MutableMap[String, List[String]] = MutableMap()

  try {
    val file = Source.fromFile(sourceFile)
    val sourceLines = file.getLines.toList
    //val cleanedLines = sourceLines.map { _.replace("\t", " ") }

    val remoteQueue: MutableQueue[String] = MutableQueue()
    var codes = false 

    sourceLines.foreach { line =>
      val cleanLine = line.replace("\t", " ").trim
      cleanLine match {
        case "begin remote" => remoteQueue.clear
        case nameLine if cleanLine.indexOf("name") == 0 => 
          // enqueue the remote name
          remoteQueue.enqueue(nameLine.split(" ").last)
        case "begin codes" => codes = true
        case "end codes" => codes = false
        case "end remote" =>
          // move everything to the map
          auxRemoteCodes += (remoteQueue.dequeue -> remoteQueue.toList)
        case codeLine if codes == true =>
          // enqueue the remote code
          remoteQueue.enqueue(codeLine.split(" ").head)
        case _ =>
      }
    }
     
  } catch {
    case e: FileNotFoundException => 
      println(s"LIRC source file $sourceFile was not found!")
    case e: IOException => 
      println(s"IOException trying to read LIRC source file $sourceFile", e.printStackTrace)
  }

  val REMOTE_CODES: Map[String, List[String]] = auxRemoteCodes.toMap

  def devices: Iterable[String] = REMOTE_CODES.keys
  def codesForRemote(remote: String): List[String] = REMOTE_CODES.get(remote).getOrElse(List.empty)
  def supportsCode(remote: String, code: String): Boolean = 
    codesForRemote(remote).contains(code)
  def supportsRemote(remote: String): Boolean = 
    REMOTE_CODES.keys.toList.contains(remote)

  def sendOnce(remote: String, code: String): Boolean = {
    if (!supportsRemote(remote)) {
      println(s"Remote $remote is not a valid device")
      false
    } else if (!supportsCode(remote, code)) {
      println(s"Remote $remote does not support code $code")
      false
    } else {
      s"irsend SEND_ONCE $remote $code" ! match {
        case 0 => true
        case error if error > 0 => 
          println(s"Could not run `irsend SEND_ONCE $remote $code`")
          false
      }
    }
  }

}
