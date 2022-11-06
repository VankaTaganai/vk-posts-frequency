package am.panov.vk

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.server.StubServer
import org.http4s.blaze.client.BlazeClientBuilder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class VKFeedTrackerStubSpec extends AnyFlatSpec with Matchers {

  private implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

  private val PORT: Int = 8083

  it should "Collect stats for 24 hour" in {
    val stubServer = new StubServer(PORT)

    whenHttp(stubServer).`match`().`then`(Action.stringContent("{\"response\": {\"count\": 1}}"))

    stubServer.run()

    val assertion = BlazeClientBuilder[IO].resource.use { client =>
      val vkFeedService = new VKFeedService[IO](VKConfig(s"http://localhost:$PORT", "TOKEN", "1"), client)
      val vkFeedTracker = new VKFeedTracker[IO](vkFeedService)

      val stats = vkFeedTracker.collectStats("ff", 24)

      stats
    }.unsafeToCompletableFuture().join() shouldBe (0 until 24).map(_ => 1).toList

    stubServer.stop()

    assertion
  }
}
