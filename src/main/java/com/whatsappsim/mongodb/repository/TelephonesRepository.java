package com.whatsappsim.mongodb.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.whatsappsim.mongodb.model.Telephone;

@Repository
public class TelephonesRepository {

	private final MongoTemplate mongoTemplate;
	
    @Autowired
    public TelephonesRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

	public List<Telephone> getRequestsByTelephone(String telephone) {
		//Direction direction = (order.equals("1"))?Direction.ASC:Direction.DESC;
    	Query query = new Query();
    	query.addCriteria(Criteria.where("telephone").is(telephone));
    	//.with(new Sort(direction, orderBy));
        return mongoTemplate.find(query, Telephone.class);     
	}

	public Telephone getTelephoneByCommunicationIdAndTelephoneTo(String communicationId, String telephoneTo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("communicationId").is(communicationId))
		.addCriteria(Criteria.where("telephoneTo").is(telephoneTo))
		.fields().include("telephoneTo")
		.include("description");
		return mongoTemplate.findOne(query, Telephone.class);
	}
	
	public Integer updateAdvicesByCommunicationId(String communicationId, String telephone) {
		Query query = new Query();
		query.addCriteria(Criteria.where("communicationId").is(communicationId))
		.addCriteria(Criteria.where("telephone").is(telephone));
		Update update = new Update().inc("advices", 1);
		Telephone request = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Telephone.class);
		return request.getAdvices();
	}
	
	public void removeAdvicesByCommunicationId(String communicationId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("communicationId").is(communicationId));
		Update update = new Update().unset("advices");
		mongoTemplate.updateMulti(query, update, Telephone.class);
	}
	
	/**
	 * If there is no other telephone inserted we insert two, one to and other from
	 */
	public Telephone addTelephonesCommunication(Telephone telephone) {
		Date currentDate = new Date();
		String uuid = UUID.randomUUID().toString();
		
		telephone.setCommunicationId(uuid);
		telephone.setDate(currentDate);
		telephone = mongoTemplate.insert(telephone);

		Telephone telephoneObjectTo = new Telephone();
		telephoneObjectTo.setCommunicationId(uuid);
		telephoneObjectTo.setDate(currentDate);
		telephoneObjectTo.setTelephone(telephone.getTelephoneTo());
		telephoneObjectTo.setTelephoneTo(telephone.getTelephone());
		telephoneObjectTo.setDescription(telephone.getTelephone());
		mongoTemplate.insert(telephoneObjectTo);
		
		return telephone;
	}
	
	/**
	 * Search a communication between telephone and telephoneTo
	 * if it is found, updates description
	 * @param telephone
	 */
	public void updateTelephone(Telephone telephone) {
		Query query = new Query();
		query.addCriteria(Criteria.where("telephone").is(telephone.getTelephone()))
		.addCriteria(Criteria.where("telephoneTo").is(telephone.getTelephoneTo()));
		Telephone telephoneFind = mongoTemplate.findOne(query, Telephone.class);
		if (null != telephoneFind) {
			Update update = Update.update("description", telephone.getDescription());
			mongoTemplate.updateFirst(query, update, Telephone.class);
			telephone.setCommunicationId(telephoneFind.getCommunicationId());
		}
	}
	
}
