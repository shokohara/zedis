package zedis.free

import scala.collection.JavaConversions

import scalaz.Id.Id
import scalaz.{Free, Kleisli, Monad, ~>}
import scalaz.std.option._
import redis.clients.jedis.Jedis

import zedis.free.commands.JedisCommands

object jedis extends JedisCommands {

  type JedisCommand[A] = Free[JedisCommandOp, A]
  implicit val JedisCommandMonad: Monad[JedisCommand] = Free.freeMonad[JedisCommandOp]

  def interpK[M[_]: Monad]: JedisCommandOp ~> ({type F[A] = Jedis => M[A]})#F =
    new (JedisCommandOp ~> ({type F[A] = Jedis => M[A]})#F) {
      def apply[A](fa: JedisCommandOp[A]): Jedis => M[A] =
        (j: Jedis) => fa.command[M].run(j)
    }

  def trans[M[_]: Monad](session: Jedis): ({type F[A] = Jedis => M[A]})#F ~> M =
    new (({type F[A] = Jedis => M[A]})#F ~> M ) {
      def apply[A](fa: Jedis => M[A]) =
        fa(session)
    }

  def runCommand[M[_]: Monad, A](program: JedisCommand[A], session: Jedis): M[A] =
    program.foldMap(interpK[M] andThen trans[M](session))

  implicit class JedisCommandOps[M[_]: Monad, A](ma: JedisCommand[A]) {
    def exec(session: Jedis): A = runCommand[Id, A](ma, session)
    def execOpt(session: Jedis): Option[A] = runCommand[Option, A](ma, session)
  }
}
