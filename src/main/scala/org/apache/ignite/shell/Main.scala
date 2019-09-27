package org.apache.ignite.shell

import java.util

import org.apache.ignite.Ignition
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder

object Main extends App {

  // Parse input
  var configFile : Option[String] = None
  var clientMode = true

  def nextOption(list : List[String]) : Unit = {
    list match {
      case Nil => ()
      case "-c" :: value :: tail =>
        clientMode = value match {
          case "false" | "0" => false
          case "true" | "1" => true
          case _ =>
            System.err.println("invalid client mode flag. -c [true|1|false|0], defaulting to true")
            true
        }
        nextOption(tail)
      case value :: tail =>
        configFile = Some(value)
        nextOption(tail)
    }
  }
  nextOption(args.toList)

  val ignite =  {
    Ignition.setClientMode(clientMode)
    configFile match {
      case Some(cfg) =>
        Ignition.start(cfg)
      case _ =>
        val igniteConfiguration = new IgniteConfiguration()
        igniteConfiguration.setPeerClassLoadingEnabled(true)

        val ipFinder = new TcpDiscoveryVmIpFinder()
        val addresses = util.Arrays.asList("127.0.0.1")
        ipFinder.setAddresses(addresses)

        val discoverySpi = new TcpDiscoverySpi()
        discoverySpi.setIpFinder(ipFinder)
        igniteConfiguration.setDiscoverySpi(discoverySpi)

        Ignition.start(igniteConfiguration)
    }
  }

  // Let's get this show on the road
  ammonite.Main(
     predefCode = "println (\"Starting Ignite shell...\")",
    welcomeBanner = Some("Welcome to the Ignite shell. The variable 'ignite' is ready for use.")
  ).run(
    "ignite" -> ignite
  )

  ignite.close()
}
