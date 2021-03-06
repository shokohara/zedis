package zedis
package command

import scalaz.Free
import zedis.adt.CommandADT, CommandADT._

object key extends KeyCommand

trait KeyCommand {

  def del(key: String, keys: String*): RedisCommand[Long] =
    Free.liftF[CommandADT, Long](DEL(nel(key, keys)))

  def dump(key: String): RedisCommand[Array[Byte]] =
    Free.liftF[CommandADT, Array[Byte]](DUMP(key))

  def exists(key: String): RedisCommand[Boolean] =
    Free.liftF[CommandADT, Boolean](EXISTS(key))

  def expire(key: String, seconds: Long): RedisCommand[Boolean] =
    Free.liftF[CommandADT, Boolean](EXPIRE(key, seconds))

  def expireat(key: String, unixTime: Long): RedisCommand[Boolean] =
    Free.liftF[CommandADT, Boolean](EXPIREAT(key, unixTime))

  def keys(pattern: String): RedisCommand[Set[String]] =
    Free.liftF[CommandADT, Set[String]](KEYS(pattern))

  // def migrate

  def move(key: String, dbIndex: Int): RedisCommand[Boolean] =
    Free.liftF[CommandADT, Boolean](MOVE(key, dbIndex))

  // def object

  def persist(key: String): RedisCommand[Boolean] =
    Free.liftF[CommandADT, Boolean](PERSIST(key))

  def pexpire(key: String, milliseconds: Long): RedisCommand[Long] =
    Free.liftF[CommandADT, Long](PEXPIRE(key, milliseconds))

  def pexpireat(key: String, millisecondsUnixTime: Long): RedisCommand[Long] =
    Free.liftF[CommandADT, Long](PEXPIREAT(key, millisecondsUnixTime))

  def pttl(key: String): RedisCommand[Long] =
    Free.liftF[CommandADT, Long](PTTL(key))

  def randomkey: RedisCommand[String] =
    Free.liftF[CommandADT, String](RANDOMKEY)

  def rename(oldKey: String, newKey: String): RedisCommand[String] =
    Free.liftF[CommandADT, String](RENAME(oldKey, newKey))

  def renamenx(oldKey: String, newKey: String): RedisCommand[Boolean] =
    Free.liftF[CommandADT, Boolean](RENAMENX(oldKey, newKey))

  def restore(key: String, milliseconds: Long, data: Array[Byte]): RedisCommand[String] =
    Free.liftF[CommandADT, String](RESTORE(key, milliseconds, data))

  // def scan

  // def sort

  def ttl(key: String): RedisCommand[Long] =
    Free.liftF[CommandADT, Long](TTL(key))

  // type is reserved word ...
  def valuetype(key: String): RedisCommand[String] =
    Free.liftF[CommandADT, String](VALUETYPE(key))

  // def wait
}
