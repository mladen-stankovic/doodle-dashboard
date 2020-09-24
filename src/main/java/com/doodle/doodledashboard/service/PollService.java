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
     * Find polls for provided initiator email
     *
     * @param initiatorEmail provided initiator email
     * @return List of {@link Document} objects in polls collection for provided initiator email
     */
    public List<Document> findByInitiatorEmail(String initiatorEmail) {
        return getDocuments(Criteria.where("initiator.email").is(initiatorEmail));
    }

    /**
     * Search all polls collection for provided title
     *
     * @param title provided title
     * @return List of {@link Document} objects in polls collection for provided title
     */
    public List<Document> searchByTitle(String title) {
        return getDocuments(Criteria.where("title").regex(".*" + title + ".*"));
    }

    /**
     * Find all polls created after provided date
     *
     * @param date provided date
     * @return List of {@link Document} objects in polls collection created after provided date
     */
    public List<Document> findCreatedAfterDate(Long date) {
        return getDocuments(Criteria.where("initiated").gt(date));
    }

    private List<Document> getDocuments(Criteria criteria) {
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Document.class, POLLS_COLLECTION);
    }
}
