package am.panov.vk

import cats.effect._
import cats.syntax.all._
import java.time.temporal.ChronoUnit.MINUTES
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

case class VKFeedTracker[F[_]: Async](vkService: VKFeedService[F]) {
  def collectStats(tag: String, hours: Int): F[List[Int]] = {
    val hour = FiniteDuration(60, TimeUnit.MINUTES)

    for {
      curTime   <- Clock[F].realTimeInstant
      startTime <- (0 until hours).map(t => curTime.minus((t + 1) * 60, MINUTES).getEpochSecond).reverse.toList.pure
      res       <- startTime.traverse(st => vkService.postsCount(tag, st, st + hour.toSeconds))
    } yield res
  }
}
