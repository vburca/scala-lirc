scala-lirc
==========

Simple Scala wrapper for the [LIRC library](http://www.lirc.org/).

*Based on its [Python cousin](https://github.com/loisaidasam/lirc-python)*

#### Features

* can send simple one-time messages through `irsend SEND_ONCE`
* supports multiple remotes in the *lircd.conf*


## Usage

Below is an example of usage.

```
scala> import scalalirc.ScalaLirc
import scalalirc.ScalaLirc

scala> val lirc = new ScalaLirc()
lirc: scalalirc.ScalaLirc = scalalirc.ScalaLirc@658bbfd8

scala> lirc.devices
res0: Iterable[String] = Set(TestRemote1, TestRemote2)

scala> lirc.supportsCode("TestRemote1", "BTN_START")
res1: Boolean = true

scala> lirc.sendOnce("TestRemote1", "BTNSTART")
Remote TestRemote1 does not support code BTNSTART
res2: Boolean = false

scala> lirc.sendOnce("TestRemote1", "BTN_START")    // LIRC is not available on this platform
Error trying to execute `irsend SEND_ONCE TestRemote1 BTN_START`: java.io.IOException: error=2, No such file or directory
res3: Boolean = false

scala> lirc.sendOnce("AmbiLite", "BTN_START")   // LIRC is now available
res4: Boolean = true
```

#### Custom location of *lircd.conf*
If, for some reason, you want to use a custom *lircd.conf*, or the location is not the standard one (`/etc/lirc/lircd.conf`),
you can create the `ScalaLirc` object in the following way:

```
val lirc = new ScalaLirc("path/to/your/lircd.conf")
```



## Issues

Right now `scala-lirc` only supports the `SEND_ONCE` feature of LIRC. 

If you would also like to see other LIRC features added to `scala-lirc`, or if you find something wrong with the current implementation,
create a *Pull Request* or [let me know](https://github.com/vburca/scala-lirc/issues/new?title=New%20LIRC%20Feature) please!


Vlad Burca (vburca) 2016
