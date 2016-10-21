package keyvent.example1.grpc

import java.util.logging.Logger

import io.grpc.{Server, ServerBuilder}
import keyvent.core.{UnitOfWorkId, Version}
import keyvent.example1.CompanyEvent.TestOneof.{Activated, Created}
import keyvent.example1._

import scala.concurrent.{ExecutionContext, Future}

object Example1Server {

  private val logger = Logger.getLogger(classOf[Example1Server].getName)

  def main(args: Array[String]): Unit = {
    val server = new Example1Server(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }

  private val port = 50051
}

class Example1Server(executionContext: ExecutionContext) { self =>

  private[this] var server: Server = null

  private def start(): Unit = {

    server = ServerBuilder.forPort(Example1Server.port)
      .addService(CustomerCommandsGrpc.bindService(new CustomerCommandsImpl, executionContext))
      .build
      .start

    Example1Server.logger.info("Server started, listening on " + Example1Server.port)

    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        println("*** shutting down gRPC server since JVM is shutting down")
        self.stop()
        println("*** server shut down")
      }
    })

  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class CustomerCommandsImpl extends CustomerCommandsGrpc.CustomerCommands {
    override def create(cmd: CreateCustomer) = {
      println("*** create " + cmd)
      val reply = CompanyUnitOfWork(UnitOfWorkId("uow-1"), cmd.commandId, cmd.entityId.get, Version(1L),
        Seq(CompanyEvent(Created(CustomerCreated(cmd.entityId.get)))))
      println("*** reply " + reply)
      Future.successful(reply)
    }
    override def activate(cmd: ActivateCustomer) = {
      println("*** activate " + cmd)
      val reply = CompanyUnitOfWork(UnitOfWorkId("uow-2"), cmd.commandId, cmd.entityId, Version(1L),
        Seq(CompanyEvent(Activated(CustomerActivated(cmd.entityId)))))
      println("*** reply " + reply)
      Future.successful(reply)
    }
  }

}
