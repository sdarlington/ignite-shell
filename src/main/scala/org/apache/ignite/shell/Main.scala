package org.apache.ignite.shell

import java.util

import org.apache.ignite.Ignition
import org.apache.ignite.configuration.{ClientConfiguration, IgniteConfiguration}
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder

object ConnectionType extends Enumeration {
  type ConnectionType = Value
  val SERVER, THICK, THIN = Value
}
object Main extends App {

  // Parse input
  var configFile : Option[String] = None
  var connectionString : Option[String] = None
  var connectionType = ConnectionType.THICK

  def nextOption(list : List[String]) : Unit = {
    list match {
      case Nil => ()
      case "-c" :: value :: tail =>
        connectionType = value match {
          case "server" | "s" => ConnectionType.SERVER
          case "client" | "c" => ConnectionType.THICK
          case "thin" | "t" => ConnectionType.THIN
          case _ =>
            System.err.println("invalid connection mode flag. -c [thick|thin|server], defaulting to thick.")
            ConnectionType.THICK
        }
        nextOption(tail)
      case "-s" :: value :: tail =>
        connectionString = Some(value)
        nextOption(tail)
      case value :: tail =>
        configFile = Some(value)
        nextOption(tail)
    }
  }
  nextOption(args.toList)

  // Let's get this show on the road
  val cli = ammonite.Main(
     predefCode = "println (\"Starting Ignite shell...\")",
     welcomeBanner = Some("Welcome to the Ignite shell. The variable 'ignite' is ready for use.")
  )

  connectionType match {
    case ConnectionType.THIN =>
      val clientConfiguration = new ClientConfiguration()
      clientConfiguration.setAddresses(connectionString.getOrElse("127.0.0.1"))
      val ignite = Ignition.startClient(clientConfiguration)
      cli.run("ignite" -> ignite)
      ignite.close()
    case ConnectionType.THICK | ConnectionType.SERVER =>
      val ignite =  {
        System.setProperty("IGNITE_NO_ASCII", "1")
        Ignition.setClientMode(connectionType == ConnectionType.THICK)
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
      cli.run("ignite" -> ignite)
      ignite.close()
  }

}
