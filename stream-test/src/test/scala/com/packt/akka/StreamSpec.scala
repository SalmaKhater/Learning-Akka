import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ TestKit, TestActorRef, TestProbe, ImplicitSender }
import org.scalatest.matchers.MustMatchers
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll }
import akka.stream.scaladsl._
import scala.concurrent._
import scala.concurrent.duration._
import akka.stream.{ ActorMaterializer, OverflowStrategy}
import akka.pattern.pipe

class StreamActorSpec extends TestKit(ActorSystem("test-system"))
                  with ImplicitSender
                  with FlatSpecLike
                  with BeforeAndAfterAll
                  with MustMatchers {
  import system.dispatcher

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  implicit val flowMaterializer = ActorMaterializer()

  "With TestKit" should "test actor receive elements from the sink" in {
    val source = Source(1 to 4).grouped(2)

    val testPrope = TestProbe()

    source.runWith(Sink.head).pipeTo(testPrope.ref)

    testPrope.expectMsg(Seq(1, 2))
  }

  it should "have a control over a receiving elements" in {
    case object Tick

    val source = Source(0.millis, 200.millis, Tick)

    val probe = TestProbe()

    val sink = Sink.actorRef(probe.ref, "completed")

    val runnable = source.to(sink).run()

    probe.expectMsg(1.second, Tick)
    probe.expectNoMsg(100.millis)
    probe.expectMsg(200.millis, Tick)
    runnable.cancel()
    probe.expectMsg(200.millis, "completed")
  }

  it should "have a control over elements to be sent" in {
    val sink = Flow[Int].map(_.toString).toMat(Sink.fold("")(_ + _))(Keep.right)

    val source = Source.actorRef(8, overflowStrategy = OverflowStrategy.fail)

    val (ref, result) = source.toMat(sink)(Keep.both).run()

    ref ! 1
    ref ! 2
    ref ! 3
    ref ! akka.actor.Status.Success("done")

    Await.result(result, 200.millis) must equal("123")
  }


}