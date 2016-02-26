package scalalirc

import scala.collection.mutable.{Queue => MutableQueue, Map => MutableMap}
import scala.io.Source

import sys.process._

import java.io.{FileNotFoundException, IOException}

class ScalaLirc(sourceFile: String = "/etc/lirc/lircd.conf") {
  
  val auxRemoteCodes: MutableMap[String, List[String]] = MutableMap()

  try {
    val file = Source.fromFile(sourceFile)
    val sourceLines = file.getLines.toList

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
      System.err.println(s"LIRC source file $sourceFile was not found!")
    case e: IOException => 
      System.err.println(s"IOException trying to read LIRC source file $sourceFile", e.printStackTrace)
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
      System.err.println(s"Remote $remote is not a valid device")
      false
    } else if (!supportsCode(remote, code)) {
      System.err.println(s"Remote $remote does not support code $code")
      false
    } else {
      val command = s"irsend SEND_ONCE $remote $code"
      try {
        command ! match {
          case 0 => true
          case error if error > 0 => 
            System.err.println(s"Could not run `$command`")
            false
        }
      } catch {
        case e: IOException => 
          System.err.println(s"Error trying to execute `$command`: ${e.getCause}")
          false
      }
    }
  }

}
