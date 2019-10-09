package com.whatsappsim.mongodb.repository;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.whatsappsim.mongodb.model.Message;

@Repository
public class MessagesRepository {

	private final MongoTemplate mongoTemplate;
	
    @Autowired
    public MessagesRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    public Message insertMessage(Message message){
    	mongoTemplate.save(message);
    	return message;
	}
    
    public void updateMessage(String communicationId, String telephone){
    	Query query = new Query();
    	query.addCriteria(Criteria.where("communicationId").is(communicationId)
    			.and("telephone").is(telephone)
    			.and("dates.receive").exists(false));
    	
    	//List<Message> lista = mongoTemplate.find(query, Message.class);
    	Update update = new Update();
        update.set("dates.receive", new Date().getTime());
        mongoTemplate.updateMulti(query, update, Message.class);
	}

    public List<Message> getMessageByRequestId(String communicationId){
		Query query = new Query();
		query.addCriteria(Criteria.where("communicationId").is(communicationId));
		
		query.fields().include("id");
		query.fields().include("telephone");
		query.fields().include("telephoneTo");
		query.fields().include("text");
		query.fields().include("images.thumbnail");
		query.fields().include("videos.thumbnail");
		query.fields().include("docs.thumbnail");
		query.fields().include("dates");
		return mongoTemplate.find(query, Message.class);
    }
    
    public List<Message> findMessageByRequestId(String communicationId) {
    	Query query = new Query();
    	query.addCriteria(Criteria.where("communicationId").is(communicationId));
    	return mongoTemplate.find(query, Message.class);
    }
	
    public Message getFileByMessageIdAndType(String messageId, String type) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(messageId)));
		query.fields().include(type+".name");
		query.fields().include(type+".real");
		return mongoTemplate.findOne(query, Message.class);
	}


}
