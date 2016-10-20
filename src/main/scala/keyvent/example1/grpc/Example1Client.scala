package keyvent.example1.grpc

import java.util.concurrent.TimeUnit
import java.util.logging.{Level, Logger}

import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
import keyvent.core.{CommandId, EntityId}
import keyvent.example1.CustomerCommandsGrpc.CustomerCommandsBlockingStub
import keyvent.example1.{CreateCustomer, CustomerCommandsGrpc}

object Example1Client {

  def apply(host: String, port: Int): Example1Client = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
    val blockingStub = CustomerCommandsGrpc.blockingStub(channel)
    new Example1Client(channel, blockingStub)
  }

  def main(args: Array[String]): Unit = {
    val client = Example1Client("localhost", 50051)
    try {
      val cmd1: CreateCustomer = CreateCustomer(CommandId("cmd-1"), Some(EntityId("entity-1")), foo = Some("teste"))
      client.submit(cmd1)
    } finally {
      client.shutdown()
    }
  }

}

class Example1Client private(
                              private val channel: ManagedChannel,
                              private val blockingStub: CustomerCommandsBlockingStub
                            ) {

  private[this] val logger = Logger.getLogger(classOf[Example1Client].getName)

  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def submit(cmd: CreateCustomer): Unit = {

    logger.info("Will try to submit command " + cmd + " ...")
    try {
      val response = blockingStub.create(cmd)
      logger.info("Response: " + response.message)
    }
    catch {
      case e: StatusRuntimeException =>
        logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus)
    }
  }

}
