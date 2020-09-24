package com.doodle.doodledashboard.unit;

import com.doodle.doodledashboard.common.DataConstants;
import com.doodle.doodledashboard.service.PollService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.bson.Document;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;

/**
 * Created by mladen.stankovic on 2020-09-24.
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PollServiceUnitTests {
    @InjectMocks
    private PollService pollService;

    @Mock
    private MongoTemplate mongoTemplate;

    private void givenUserHasPolls() {
        Query query = new Query();
        query.addCriteria(Criteria.where("initiator.email").is(DataConstants.TEST_INITIATOR));

        List<Document> documents = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            Document document = new Document();
            Document initiator = new Document();
            initiator.put("email", DataConstants.TEST_INITIATOR);
            document.put("initiator", initiator);
            documents.add(document);
        }
        when(mongoTemplate.find(query, Document.class,DataConstants.POLLS_COLLECTION)).thenReturn(documents);
    }

    @Test
    public void givenUserHasPolls_whenSearchingForPolls_checkResponse() {
        givenUserHasPolls();

        List<Document> documents = pollService.findByInitiatorEmail(DataConstants.TEST_INITIATOR);
        Assertions.assertNotNull(documents);
        Assertions.assertNotEquals(documents.size(), 0);
        documents.forEach(d -> hasProperty("initiator", hasProperty("email", equalTo(DataConstants.TEST_INITIATOR))));
    }
}
