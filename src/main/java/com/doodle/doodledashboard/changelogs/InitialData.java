package com.doodle.doodledashboard.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
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
                Type listType = new TypeToken<List<Document>>() {}.getType();
                File initialData = ResourceUtils.getFile("classpath:polls_initial_data.json");
                List<Document> polls = new Gson().fromJson(new FileReader(initialData), listType);
                polls.forEach(p -> {
                    p.put("_id", p.get("id"));
                    p.remove("id");
                    db.getCollection("polls").insertOne(p);
                });

                //add indexes
                db.getCollection("polls").createIndex(Indexes.ascending("initiated"));
                db.getCollection("polls").createIndex(Indexes.ascending("initiator.name"));
            } catch (FileNotFoundException e) {
                logger.error("File not found.");
            }
        }
    }
}
