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
import java.util.UUID;

import static com.doodle.doodledashboard.common.DataConstants.POLLS_COLLECTION;
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

    private Query givenUserHasPolls() {
        Query query = new Query();
        query.addCriteria(Criteria.where("initiator.email").is(DataConstants.TEST_INITIATOR_1));

        List<Document> documents = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            Document document = new Document();
            Document initiator = new Document();
            initiator.put("email", DataConstants.TEST_INITIATOR_1);
            document.put("initiator", initiator);
            documents.add(document);
        }
        when(mongoTemplate.find(query, Document.class, DataConstants.POLLS_COLLECTION)).thenReturn(documents);

        return query;
    }

    private Query givenSearchByTitleHasResults() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").regex(".*" + DataConstants.SEARCH_TERM + ".*"));

        List<Document> documents = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            Document document = new Document();
            document.put("title", UUID.randomUUID().toString() + DataConstants.SEARCH_TERM + UUID.randomUUID().toString());
            documents.add(document);
        }
        when(mongoTemplate.find(query, Document.class, DataConstants.POLLS_COLLECTION)).thenReturn(documents);

        return query;
    }

    @Test
    public void givenUserHasPolls_whenSearchingForPolls_checkResponse() {
        Query query = givenUserHasPolls();

        List<Document> documents = pollService.findByInitiatorEmail(DataConstants.TEST_INITIATOR_1);
        Assertions.assertNotNull(documents);
        Assertions.assertNotEquals(documents.size(), 0);
        documents.forEach(d -> {
            Assertions.assertNotNull(d.get("initiator"));
            Assertions.assertEquals(((Document)d.get("initiator")).get("email"), DataConstants.TEST_INITIATOR_1);
        });

        verify(mongoTemplate, times(1)).find(query, Document.class, POLLS_COLLECTION);
    }

    @Test
    public void givenSearchByTitleHasResults_whenSearchingForPolls_checkResponse() {
        Query query = givenSearchByTitleHasResults();

        List<Document> documents = pollService.searchByTitle(DataConstants.SEARCH_TERM);
        Assertions.assertNotNull(documents);
        Assertions.assertNotEquals(documents.size(), 0);
        documents.forEach(d -> {
            Assertions.assertNotNull(d.get("title"));
            Assertions.assertTrue(d.getString("title").contains(DataConstants.SEARCH_TERM));
        });

        verify(mongoTemplate, times(1)).find(query, Document.class, POLLS_COLLECTION);
    }
}
