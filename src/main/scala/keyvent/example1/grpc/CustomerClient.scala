package keyvent.example1.grpc

import java.util.concurrent.TimeUnit
import java.util.logging.{Level, Logger}

import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
import keyvent.core.{CommandId, EntityId}
import keyvent.domain1.CustomerCommandsGrpc.CustomerCommandsBlockingStub
import keyvent.domain1.{CreateCustomer, CustomerCommandsGrpc}

object CustomerClient {

  def apply(host: String, port: Int): CustomerClient = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
    val blockingStub = CustomerCommandsGrpc.blockingStub(channel)
    new CustomerClient(channel, blockingStub)
  }

  def main(args: Array[String]): Unit = {
    val client = CustomerClient("localhost", 50051)
    try {
      val cmd1: CreateCustomer = CreateCustomer(CommandId("cmd-1"), Some(EntityId("entity-1")), foo = Some("teste"))
      client.submit(cmd1)
    } finally {
      client.shutdown()
    }
  }

}

class CustomerClient private(
                              private val channel: ManagedChannel,
                              private val blockingStub: CustomerCommandsBlockingStub
                            ) {

  private[this] val logger = Logger.getLogger(classOf[CustomerClient].getName)

  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def submit(cmd: CreateCustomer): Unit = {

    logger.info("Will try to submit command " + cmd + " ...")
    try {
      val response = blockingStub.create(cmd)
      logger.info("Response: " + response)
    }
    catch {
      case e: StatusRuntimeException =>
        logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus)
    }
  }

}
