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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static com.doodle.doodledashboard.common.DataConstants.TEST_DATE_IN_THE_PAST;
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
        List<Document> documents = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            Document document = new Document();
            Document initiator = new Document();
            initiator.put("email", DataConstants.TEST_INITIATOR_1);
            document.put("initiator", initiator);
            documents.add(document);
        }
        when(mongoTemplate.find(any(Query.class), eq(Document.class), any(String.class))).thenReturn(documents);
    }

    private void givenSearchByTitleHasResults() {
        List<Document> documents = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            Document document = new Document();
            document.put("title", UUID.randomUUID().toString() + DataConstants.SEARCH_TERM + UUID.randomUUID().toString());
            documents.add(document);
        }
        when(mongoTemplate.find(any(Query.class), eq(Document.class), any(String.class))).thenReturn(documents);
    }

    private void givenThereArePollsAfterProvidedDate() {
        List<Document> documents = Lists.newArrayList();
        Document document = new Document();
        document.put("initiated", Long.parseLong(TEST_DATE_IN_THE_PAST) + 10000000);
        documents.add(document);
        when(mongoTemplate.find(any(Query.class), eq(Document.class), any(String.class))).thenReturn(documents);
    }

    @Test
    public void givenUserHasPolls_whenSearchingForPolls_checkResponse() {
        givenUserHasPolls();

        List<Document> documents = pollService.findByInitiatorEmail(DataConstants.TEST_INITIATOR_1);
        Assertions.assertNotNull(documents);
        Assertions.assertNotEquals(documents.size(), 0);
        documents.forEach(d -> {
            Assertions.assertNotNull(d.get("initiator"));
            Assertions.assertEquals(((Document)d.get("initiator")).get("email"), DataConstants.TEST_INITIATOR_1);
        });

        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Document.class), any(String.class));
    }

    @Test
    public void givenSearchByTitleHasResults_whenSearchingForPolls_checkResponse() {
        givenSearchByTitleHasResults();

        List<Document> documents = pollService.searchByTitle(DataConstants.SEARCH_TERM);
        Assertions.assertNotNull(documents);
        Assertions.assertNotEquals(documents.size(), 0);
        documents.forEach(d -> {
            Assertions.assertNotNull(d.get("title"));
            Assertions.assertTrue(d.getString("title").contains(DataConstants.SEARCH_TERM));
        });

        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Document.class), any(String.class));
    }

    @Test
    public void givenThereArePollsAfterProvidedDate_whenSearchingForPolls_checkResponse() {
        givenThereArePollsAfterProvidedDate();

        Long testDateInThePast = Long.parseLong(TEST_DATE_IN_THE_PAST);
        List<Document> documents = pollService.findCreatedAfterDate(testDateInThePast);
        Assertions.assertNotNull(documents);
        Assertions.assertNotEquals(documents.size(), 0);
        documents.forEach(d -> {
            Assertions.assertNotNull(d.get("initiated"));
            Assertions.assertTrue(d.getLong("initiated") > testDateInThePast);
        });

        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Document.class), any(String.class));
    }
}
