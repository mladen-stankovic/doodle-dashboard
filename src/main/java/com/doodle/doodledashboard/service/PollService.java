package com.doodle.doodledashboard.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.doodle.doodledashboard.common.DataConstants.POLLS_COLLECTION;

/**
 * Created by mladen.stankovic on 2020-09-24.
 */
@Service
public class PollService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PollService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Find all {@link Document} objects in polls collection for provided initiator email
     *
     * @param initiatorEmail provided initiator email
     * @return List of {@link Document} objects in polls collection for provided initiator email
     */
    public List<Document> findByInitiatorEmail(String initiatorEmail) {
        Query query = new Query();
        query.addCriteria(Criteria.where("initiator.email").is(initiatorEmail));
        return mongoTemplate.find(query, Document.class, POLLS_COLLECTION);
    }

    /**
     * Search all {@link Document} objects in polls collection for provided title
     *
     * @param title provided title
     * @return List of {@link Document} objects in polls collection for provided title
     */
    public List<Document> searchByTitle(String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").regex(".*" + title + ".*"));
        return mongoTemplate.find(query, Document.class, POLLS_COLLECTION);
    }
}
