package keyvent.example1

import java.time.LocalDateTime

import com.trueaccord.scalapb.GeneratedMessage
import com.trueaccord.scalapb.json.JsonFormat
import keyvent.core.{CommandId, EntityId}
import keyvent.domain1.CreateCustomer
import org.apache.camel.{Exchange, Processor}
import org.apache.camel.ThreadPoolRejectedPolicy._
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.{DefaultCamelContext, SimpleRegistry}
import org.apache.camel.spi.ThreadPoolProfile

object Camel {

  def start = {

    // apache camel

    val ctx = camelFactory
    ctx.addRoutes(new MyRouteBuilder())
    ctx.start();

    println("*** end camel")

  }

  def camelFactory = {

    val tp : ThreadPoolProfile = new ThreadPoolProfile("default")
    tp.setDefaultProfile(true)
    tp.setMaxQueueSize(1)
    tp.setPoolSize(1)
    tp.setMaxPoolSize(1)
    tp.setRejectedPolicy(Abort)

    val registry = new SimpleRegistry()
    registry.put("defaultProfile", tp)

    new DefaultCamelContext(registry)

  }

  class MyRouteBuilder extends RouteBuilder() {

    val jsonProcessor: Processor = new Processor {
      override def process(exchange: Exchange): Unit = {
        val result = exchange.getIn.getBody match {
          case m: GeneratedMessage => JsonFormat.toJsonString(m)
          case _  => "Keep off !"
        }
        exchange.getOut.setBody(result)
      }
    }

    override def configure() = {

      from("scheduler://test-timer?initialDelay=1000&delay=5000&useFixedDelay=true&timeUnit=MILLISECONDS")
        .routeId("scheduler-teste2")
        .doTry()
            .setBody(constant(CreateCustomer(CommandId("cmd-1"), Some(EntityId("entity-1")), foo = Some(LocalDateTime.now().toString))))
        .doCatch(classOf[Exception])
            .log("error")
        .doFinally()
            .log("finished")
        .endDoTry()
        .log("--> body: ${body}")
        .wireTap("direct:test2")

      from("direct:test2")
        .routeId("test-consumer")
        .threads(1).rejectedPolicy(Abort)
        .log("--> consuming : ${body}")
        .process(jsonProcessor)
        .log("--> as json: ${body}")

    }

  }

}

