package am.panov.vk

import cats.effect._
import cats.syntax.all._

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      config     <- IO(VKConfig.load)
      parsedArgs <- parseArgs(args)
      (tag, hours) = parsedArgs
      res <- VKFeedService.make[IO](config).use { vk =>
        new VKFeedTracker[IO](vk).collectStats(tag, hours)
      }
      _ <- IO.print("Stats: ")
      _ <- IO.println(res.mkString(", "))
    } yield ExitCode.Success

  private def parseArgs(args: List[String]): IO[(String, Int)] =
    if (args.length != 2)
      IO.raiseError(new IllegalArgumentException("The argument list should contains exactly two elements"))
    else {
      for {
        tag   <- IO.pure(args.get(0).get)
        hours <- IO.delay(args.get(1).get.toInt)
      } yield (tag, hours)
    }
}
