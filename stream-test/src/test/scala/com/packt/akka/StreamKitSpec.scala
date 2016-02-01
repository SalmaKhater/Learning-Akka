import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ TestKit, TestActorRef, TestProbe, ImplicitSender }
import org.scalatest.matchers.MustMatchers
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll }
import akka.stream.scaladsl._
import scala.concurrent._
import scala.concurrent.duration._
import akka.stream.{ ActorMaterializer, OverflowStrategy}
import akka.stream.testkit.scaladsl._

class StreamKitSpec extends TestKit(ActorSystem("test-system"))
                  with ImplicitSender
                  with FlatSpecLike
                  with BeforeAndAfterAll
                  with MustMatchers {
  import system.dispatcher

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  implicit val flowMaterializer = ActorMaterializer()

  "With Stream Test Kit" should "use a TestSink to test a source" in {
    val sourceUnderTest = Source(1 to 4).filter(_ < 3).map(_ * 2)

    sourceUnderTest.runWith(TestSink.probe[Int]())
    .request(2)
    .expectNext(2, 4)
    .expectComplete()
  }

  it should "use a TestSource to test a sink" in {
    val sinkUnderTest = Sink.cancelled

    TestSource.probe[Int]
      .toMat(sinkUnderTest)(Keep.left)
      .run()
      .expectCancellation()

  }

}