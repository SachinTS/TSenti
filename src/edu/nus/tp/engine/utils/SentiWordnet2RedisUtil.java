package edu.nus.tp.engine.utils;

import static edu.nus.tp.engine.utils.Constants.PASSWORD;
import static edu.nus.tp.engine.utils.Constants.REDIS_HOST;
import static edu.nus.tp.engine.utils.Constants.REDIS_PORT;
import static edu.nus.tp.engine.utils.Constants.SENTIWORDSCORE;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.google.common.collect.Maps;

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
		int count=0;
		
		Map<String,Double> wordScorePairs=Maps.newHashMap();
		
		while (iterator.hasNext()){
			eachLine=iterator.next();
			if (eachLine.startsWith("#")){
				continue;
			}
			count++;
			
			if (count%10000==0){
				System.out.println("Inserting record count : "+count);
			}
			
			eachLineArray=eachLine.split("\t");
			
			//System.out.println("eachLineArray[0]eachLineArray[2]:"+eachLineArray[4]+"::"+eachLineArray[2]);
			double positiveScore=Double.parseDouble(eachLineArray[2]);
			double negativeScore=Double.parseDouble(eachLineArray[3]);
			double netScore=positiveScore-negativeScore;
			
			String synonyms=eachLineArray[4];
			
			
			for (String eachSynonym : synonyms.split(" ")) {
				
				String[] eachSynonymComponents=eachSynonym.split("#");
				/*Double previousScore = jedis.zscore(SENTIWORDSCORE, eachSynonymComponents[0]);
				if (previousScore==null){
					netScore=0.0;
				}
				else{
					netScore+=previousScore;
				}*/
				//System.out.println("word : "+eachSynonym + "net score :"+netScore);
				wordScorePairs.put(eachSynonymComponents[0],netScore);
				
				if (wordScorePairs.size()%1000==0){
					persistToRedis(wordScorePairs);
					wordScorePairs=Maps.newHashMap();
				}
				
			}
			
		}
		
	}

	private void loadRedis() {
		//jedis=new Jedis("localhost", 6379);
		jedis=new Jedis(REDIS_HOST, REDIS_PORT);
		jedis.auth(PASSWORD);
		jedis.connect();
		
	}

	private void persistToRedis(Map<String,Double> wordScorePairs) {
			Transaction txn=jedis.multi();
			System.out.println("initiating transaction");
			for (Map.Entry<String, Double> eachWordScorePair : wordScorePairs.entrySet()) {
				//System.out.println("Adding entry : "+eachWordScorePair.getKey());
				//txn.zadd(SENTIWORDSCORE, eachWordScorePair.getValue(), eachWordScorePair.getKey());
				txn.zincrby(SENTIWORDSCORE, eachWordScorePair.getValue(), eachWordScorePair.getKey());
			}
			txn.exec();

	}
}
 