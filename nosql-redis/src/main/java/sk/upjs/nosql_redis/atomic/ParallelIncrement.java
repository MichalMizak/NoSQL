package sk.upjs.nosql_redis.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import sk.upjs.nosql_redis.RedisFactory;

public class ParallelIncrement {
	
	public static void main(String[] args) {
		JedisConnectionFactory factory = RedisFactory.INSTANCE.getJedisConnectionFactory();
		RedisAtomicInteger atomicInteger1 = new RedisAtomicInteger("myAI_MM", factory);
		RedisAtomicInteger atomicInteger2 = new RedisAtomicInteger("myAI_MM", factory);
		
		ExecutorService es = Executors.newCachedThreadPool();
		
		es.execute(new IncrementTask("prvy", atomicInteger1));
		es.execute(new IncrementTask("druhy", atomicInteger2));
	}
	
	private static class IncrementTask implements Runnable {

		private String name;
		private RedisAtomicInteger ai;
		
		public IncrementTask(String name, RedisAtomicInteger ai) {
			this.name = name;
			this.ai = ai;
		}
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println(name + " : " + ai.incrementAndGet());
				
			}
		}
		
	}
}
