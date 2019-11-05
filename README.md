# ignite-shell, a REPL for Apache Ignite

## About

Sometimes just being able to quickly connect to an Ignite cluster and run a simple command is all you need to do.
Ignite makes it easy to run SQL code but mst other methods typically require writing code.

ignite-shell give you a Scala REPL with a connection to your cluster. You can query all the nodes, you can run code,
testing out complex code without the time-consuming edit-compile-run-debug sequence.

Of course, as a thick client, it also gives you the possibility of bringing down the whole cluster if you have a poor
network connection or submit a complex job. In short: not recommended for any production environments. 

## Options

* `-c (client|thin|server)`. ignite-shell defaults to connecting to the cluster in client mode.
* `-s [connection string]`. If you use the thin-client, this is the hostname and port. Defaults to 127.0.0.1 and the
default port number 
* `config.xml`. If you don't specific a config file, ignite-shell will try to connect to localhost and enable
peer-class loading. 

## Building

You can create one great big JAR file with all the dependencies like this:

```sbtshell
   sbt assembly
```

This is a bit wasteful but means you don't have to construct a huge classpath before starting up. There's
an `ignite-shell.sh` script in the bin directory.

## The future

- [x] Thin client access
- [ ] Make it easier to switch between Ignite/GridGain versions
