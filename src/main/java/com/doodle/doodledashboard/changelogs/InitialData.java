package com.doodle.doodledashboard.changelogs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mladen.stankovic on 2020-09-23.
 */
@ChangeLog(order = "001")
public class InitialData extends BaseChangelog {

    private static final Logger logger = LoggerFactory.getLogger(BaseChangelog.class);

    @ChangeSet(order = "001", id = "1", author = "mladen.stankovic")
    public void populateInitialDataAndAddIndexes(MongoDatabase db, Environment environment) {
        if (devOrTestProfile(environment)) {
            try {
                //create collection polls
                db.createCollection("polls");

                //populate polls from initial json file
                ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
                Resource initialData = new ClassPathResource("polls_initial_data.json");

                List<LinkedHashMap> polls = objectMapper.readValue(new InputStreamReader(initialData.getInputStream(), "UTF-8"), List.class);
                polls.forEach(p -> {
                    p.put("_id", p.get("id"));
                    p.remove("id");
                    db.getCollection("polls").insertOne(new Document(p));
                });

                //add indexes
                db.getCollection("polls").createIndex(Indexes.ascending("initiated"));
                db.getCollection("polls").createIndex(Indexes.ascending("initiator.name"));
            } catch (Exception e) {
                logger.error("Error in populating data: " + e.getLocalizedMessage());
            }
        }
    }
}
