package keyvent.example1.grpc

import java.util.logging.Logger

import io.grpc.{Server, ServerBuilder}
import keyvent.core.CommandValidation.TestOneof
import keyvent.core.{CommandValidation, UnitOfWorkId, ValidationSuccess, Version}
import keyvent.domain1.CustomerEvent.TestOneof.{Activated, Created}
import keyvent.domain1._
import keyvent.example1.Camel.MyRouteBuilder
import org.apache.camel.impl.DefaultCamelContext

import scala.concurrent.{ExecutionContext, Future}

object CustomerServer {

  private val logger = Logger.getLogger(classOf[CustomerServer].getName)

  def main(args: Array[String]): Unit = {

    val ctx = new DefaultCamelContext();
    ctx.addRoutes(new MyRouteBuilder())
    ctx.start();

    val server = new CustomerServer(ExecutionContext.global)
    server.start()

    sys.ShutdownHookThread {
      println("** exiting")
      server.stop()
      ctx.stop()
      println("** end exiting")
    }

    //server.blockUntilShutdown()

    println("*** end")

  }

  private val port = 50051
}

class CustomerServer(executionContext: ExecutionContext) { self =>

  private[this] var server: Server = null

  private def start(): Unit = {

    server = ServerBuilder.forPort(CustomerServer.port)
      .addService(CustomerCommandsGrpc.bindService(new CustomerCommandsImpl, executionContext))
      .build
      .start

    CustomerServer.logger.info("Server started, listening on " + CustomerServer.port)

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
      val reply = CustomerUnitOfWork(UnitOfWorkId("uow-1"), cmd.commandId, cmd.entityId.get, Version(1L),
        Seq(CustomerEvent(Created(CustomerCreated(cmd.entityId.get)))))
      println("*** reply " + reply)
      Future.successful(reply)
    }
    override def activate(cmd: ActivateCustomer) = {
//      println("*** activate " + cmd)
//      val reply = CustomerUnitOfWork(UnitOfWorkId("uow-2"), cmd.commandId, cmd.entityId, Version(2L),
//        Seq(CustomerEvent(Activated(CustomerActivated(cmd.entityId)))))
//      println("*** reply " + reply)
      Future.successful(CommandValidation(TestOneof.Success(ValidationSuccess())))
    }
  }

}
