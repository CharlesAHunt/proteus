package com.cornfluence.proteus

import java.util.concurrent.{ExecutorService, Executors}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

/**
  * Spawned futures will be instantiated immediately and enqueued
  * on the unbounded LinkedBlockingQueue[Runnable] attached to the thread pool
  */
class ProteusExecutionContext(numWorkers: Int = sys.runtime.availableProcessors) {

  // underlying thread pool with a fixed number of worker threads,
  // backed by an unbounded LinkedBlockingQueue[Runnable]
  val pool: ExecutorService = Executors.newFixedThreadPool(numWorkers)

  // the ExecutionContext that wraps the thread pool
  implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(pool)
}
