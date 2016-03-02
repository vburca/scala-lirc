package scalalirc

import org.scalatest.FunSpec

class ScalaLircSpec extends FunSpec {
  describe("ScalaLirc") {
    val lirc = ScalaLirc("src/test/scala/lircd.conf.test.valid")

    it ("should exist for a valid conf file") {
      assert(lirc.isDefined)
    }

    it ("should graciously fail when conf file not present") {
      val lircNotExists = ScalaLirc("src/test/scala/does.not.exist")
      assert(!lircNotExists.isDefined)
    }

    it ("should graciously fail when conf file is not valid") {
      val lircBadFile = ScalaLirc("src/test/scala/ScalaLircSpec.scala")
      assert(!lircBadFile.isDefined)
    }

    it ("should detect remotes") {
      val remotes = Set("TestRemote1", "TestRemote2")
      assert(lirc.get.devices.toSet === remotes)
    }

    it("should detect all the codes for a remote") {
      assert(lirc.get.codesForRemote("TestRemote1").size == 4)
      assert(lirc.get.codesForRemote("TestRemote1").contains("KEY_BRIGHTNESSUP"))
    }
  }
}
