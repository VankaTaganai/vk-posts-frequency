package am.panov.vk

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.http4s.client.Client
import org.scalamock.scalatest.MockFactory
import org.scalatest.Assertion
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.{FiniteDuration, MINUTES}

class VKFeedTrackerSpec extends AnyFlatSpec with Matchers with MockFactory {
  private implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global


  private val TAG: String = "#tag"
  private val SECONDS_IN_HOUR: Long = FiniteDuration(60, MINUTES).toSeconds

  class VKFeedServiceIO(config: VKConfig, client: Client[IO]) extends VKFeedService[IO](config, client)

  it should "Collect stats for one hour" in {
    collectTest(1)
  }

  it should "Collect stats for ten hours" in {
    collectTest(10)
  }

  it should "Collect stats for twenty four hours" in {
    collectTest(24)
  }


  def collectTest(hours: Int): Assertion = {
    val vkFeedServiceMock = mock[VKFeedServiceIO]
    val tracker = VKFeedTracker[IO](vkFeedServiceMock)

    (vkFeedServiceMock.postsCount _).expects(TAG, *, *).returns(IO.pure(SECONDS_IN_HOUR.toInt)).anyNumberOfTimes()

    tracker.collectStats(TAG, hours).unsafeToCompletableFuture().join() shouldBe (0 until hours).map(_ => SECONDS_IN_HOUR).toList
  }
}
