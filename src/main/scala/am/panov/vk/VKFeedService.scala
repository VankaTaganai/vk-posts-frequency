package am.panov.vk

import cats.effect.Async
import cats.effect.kernel.Resource
import cats.syntax.all._
import derevo.circe.decoder
import derevo.derive
import org.http4s.EntityDecoder
import org.http4s.Method.GET
import org.http4s.Request
import org.http4s.Uri
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe._
import org.http4s.client.Client

class VKFeedService[F[_]: Async] private (config: VKConfig, client: Client[F]) {
  import VKFeedService._

  implicit val postsResp: EntityDecoder[F, FeedResp] = jsonOf[F, FeedResp]

  def postsCount(tag: String, startTime: Long, endTime: Long): F[Int] = {
    val params = Map(
      "q" -> tag,
      "start_time" -> startTime.toString,
      "end_time" -> endTime.toString,
      "v" -> config.version,
      "access_token" -> config.accessToken,
    )

    for {
      uri <- Async[F].fromEither(Uri.fromString(s"${config.host}/newsfeed.search"))
      uriWithParams = uri.withQueryParams(params)
      req  <- Request[F](GET, uriWithParams).pure
      resp <- client.expect[FeedResp](req)
    } yield resp.response.count
  }
}

object VKFeedService {

  def make[F[_]: Async](config: VKConfig): Resource[F, VKFeedService[F]] =
    for {
      client <- BlazeClientBuilder[F].resource
    } yield new VKFeedService[F](config, client)

  @derive(decoder)
  final case class FeedResp(response: Info)

  @derive(decoder)
  final case class Info(count: Int)
}
