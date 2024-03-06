package com.share.hy.utils;

import java.util.Random;

/**
 * SnowflakeIdWorker工具类
 *
 * @author hongsheng.wei
 */
public class SnowflakeIdWorker {

	/** 开始时间截 (2015-01-01) */
	private final static long TWEPOCH = 1420041600000L;

	/** 机器id所占的位数 */
	private final static long WORKER_ID_BITS = 5L;

	/** 数据标识id所占的位数 */
	private final static long DATACENTER_ID_BITS = 5L;

	/** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
	private final long maxWorkerId = -1L ^ (-1L << WORKER_ID_BITS);

	/** 支持的最大数据标识id，结果是31 */
	private final long maxDatacenterId = -1L ^ (-1L << DATACENTER_ID_BITS);

	/** 序列在id中占的位数 */
	private final static long SEQUENCE_BITS = 12L;

	/** 机器ID向左移12位 */
	private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

	/** 数据标识id向左移17位(12+5) */
	private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

	/** 时间截向左移22位(5+5+12) */
	private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

	/** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
	private final static long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

	/** 工作机器ID(0~31) */
	private static long workerId;

	/** 数据中心ID(0~31) */
	private static long datacenterId;

	/** 毫秒内序列(0~4095) */
	private static long sequence = 0L;

	private static long lastTimestamp = -1L;


	/**
	 * 采用随机数生成(还不完美!)
	 */
	static {
		workerId = new Random().nextInt(31);
		datacenterId = new Random().nextInt(31);
	}


	/**
	 * 构造函数
	 * @param workerId 工作ID (0~31)
	 * @param datacenterId 数据中心ID (0~31)
	 */
	public SnowflakeIdWorker(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		SnowflakeIdWorker.workerId = workerId;
		SnowflakeIdWorker.datacenterId = datacenterId;
	}



	public static String getId() {
		return Long.toString(SnowflakeIdWorker.nextId());
	}

	/**
	 *
	 * @return
	 */
	public static synchronized long nextId() {
		long timestamp = timeGen();

		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & SEQUENCE_MASK;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		}

		else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
				| (datacenterId << DATACENTER_ID_SHIFT)
				| (workerId << WORKER_ID_SHIFT)
				| sequence;
	}

	protected static long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected static long timeGen() {
		return System.currentTimeMillis();
	}




	public static void main(String [] args){
		for(int i=0; i < 100; i++) {
			long id = SnowflakeIdWorker.nextId();
			System.out.println(id);
			System.out.println(Long.toBinaryString(id));
		}

		System.out.println(-1L ^ (-1L << WORKER_ID_BITS));
		System.out.println(workerId);
		System.out.println(-1L ^ (-1L << DATACENTER_ID_BITS));
		System.out.println(datacenterId);
	}

}
