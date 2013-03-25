package edu.nus.tp.engine.utils;

import static edu.nus.tp.engine.utils.Constants.PASSWORD;
import static edu.nus.tp.engine.utils.Constants.REDIS_HOST;
import static edu.nus.tp.engine.utils.Constants.REDIS_PORT;

import java.io.FileNotFoundException;
import java.io.FileReader;

import redis.clients.jedis.Jedis;

import edu.stanford.nlp.objectbank.LineIterator;

public class SentiWordnet2RedisUtil {

	private Jedis jedis=null;
	
	private String pathToSWN = "/Users/Gabriel/Desktop/SentiWordNet_3.0.0_20130122.txt";
	
	public static void main(String[] args) throws FileNotFoundException {
		new SentiWordnet2RedisUtil().loadWordsAndScoreIntoRedis();
	}

	private void loadWordsAndScoreIntoRedis() throws FileNotFoundException {
		
		loadRedis();
		
		FileReader reader=new FileReader(pathToSWN);
		LineIterator<String> iterator=new LineIterator<String>(reader);
		
		String eachLine=null;
		String[] eachLineArray=null;
		while (iterator.hasNext()){
			eachLine=iterator.next();
			if (eachLine.startsWith("#")){
				continue;
			}
			
			
			eachLineArray=eachLine.split("\t");
			
			double positiveScore=Double.parseDouble(eachLineArray[2]);
			double negativeScore=Double.parseDouble(eachLineArray[3]);
			double netScore=positiveScore-negativeScore;
			
			String synonyms=eachLineArray[4];
			
			for (String eachSynonym : synonyms.split(" ")) {
				
				String[] eachSynonymComponents=eachSynonym.split("#");
				persistToRedis(eachSynonymComponents[0],netScore);
				
			}
			
		}
		
	}

	private void loadRedis() {
		//jedis=new Jedis("localhost", 6379);
		jedis=new Jedis(REDIS_HOST, REDIS_PORT);
		jedis.auth(PASSWORD);
		jedis.connect();
		
	}

	private void persistToRedis(String eachWord, double netScore) {
		System.out.println(eachWord + ":::"+  netScore);
		jedis.zadd("SENTIWORDSCORE", netScore, eachWord);
	}
}
