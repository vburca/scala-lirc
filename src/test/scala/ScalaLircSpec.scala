package scalalirc

import org.scalatest.FunSpec

class ScalaLircSpec extends FunSpec {
  describe("ScalaLirc") {
    val lirc = new ScalaLirc("src/test/scala/lircd.conf.test.valid")

    it ("should detect remotes") {
      val remotes = Set("TestRemote1", "TestRemote2")
      assert(lirc.devices.toSet === remotes)
    }

    it("should detect all the codes for a remote") {
      assert(lirc.codesForRemote("TestRemote1").size == 4)
      assert(lirc.codesForRemote("TestRemote1").contains("KEY_BRIGHTNESSUP"))
    }
  }
}
