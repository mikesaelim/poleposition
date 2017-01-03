package io.github.mikesaelim.poleposition.persistence;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticlePersistenceRepositoryTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ArticlePersistenceRepository repository;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TestEntityManager testEntityManager;

    @Before
    public void setUp() throws Exception {
        Lists.newArrayList(
                buildRecord("article1", "hep-ph", timestampFor(2016, 1, 15)),
                buildRecord("article2", "hep-ph", timestampFor(2016, 3, 15)),
                buildRecord("article3", "astro-ph", timestampFor(2016, 2, 16)),
                buildRecord("article4", "hep-ph", timestampFor(2016, 2, 15))
        ).forEach(testEntityManager::persist);
    }

    @Test
    public void testFindAll() throws Exception {
        List<ArticlePersistence> results = repository.findAll();
        assertEquals(4, results.size());

        Set<String> identifiers = results.stream().map(ArticlePersistence::getIdentifier).collect(toSet());
        assertTrue(identifiers.containsAll(Sets.newHashSet("article1", "article2", "article3", "article4")));
    }

    @Test
    public void testFindByPrimaryCategoryAndSubmissionTime() throws Exception {
        List<ArticlePersistence> results = repository.findByPrimaryCategoryAndSubmissionTime("hep-ph",
                timestampFor(2016, 2, 1), timestampFor(2016, 4, 1));
        assertIdentifiersInOrder(Lists.newArrayList("article4", "article2"), results);
    }

    @Test
    public void testFindByPrimaryCategoryAndSubmissionTime_CrossedTimes() throws Exception {
        List<ArticlePersistence> results = repository.findByPrimaryCategoryAndSubmissionTime("hep-ph",
                timestampFor(2016, 2, 16), timestampFor(2016, 2, 15));
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindByPrimaryCategoryAndSubmissionTime_EmptyResult() throws Exception {
        List<ArticlePersistence> results = repository.findByPrimaryCategoryAndSubmissionTime("blargh", null, null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindOne() throws Exception {
        ArticlePersistence result = repository.findOne("article3");
        assertEquals("astro-ph", result.getPrimaryCategory());
    }

    @Test
    public void testSave() throws Exception {
        repository.save(buildRecord("article5", "hep-ex", timestampFor(2016, 10, 1)));
        assertEquals(5, repository.count());
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete("article2");
        assertEquals(3, repository.count());
        assertNull(repository.findOne("article2"));
    }

    private static Timestamp timestampFor(int year, int month, int day) {
        return Timestamp.valueOf(LocalDateTime.of(year, month, day, 0, 0));
    }

    private static void assertIdentifiersInOrder(List<String> identifiers, List<ArticlePersistence> result) {
        assertEquals(identifiers, result.stream().map(ArticlePersistence::getIdentifier).collect(toList()));
    }

    private static ArticlePersistence buildRecord(String identifier, String primaryCategory, Timestamp submissionTime) {
        ArticleVersionPersistence v1 = new ArticleVersionPersistence();
        v1.setIdentifier(identifier);
        v1.setVersionNumber(1);
        v1.setSubmissionTimeUtc(submissionTime);

        ArticleVersionPersistence v2 = new ArticleVersionPersistence();
        v2.setIdentifier(identifier);
        v2.setVersionNumber(2);
        v2.setSubmissionTimeUtc(new Timestamp(submissionTime.getTime() + 100));

        ArticlePersistence article = new ArticlePersistence();
        article.setIdentifier(identifier);
        article.setPrimaryCategory(primaryCategory);
        article.setSubmissionTimeUtc(submissionTime);
        article.setRetrievalDateTimeUtc(Timestamp.valueOf(LocalDateTime.now()));
        article.setDatestamp(Date.valueOf(LocalDate.now()));
        article.setDeleted(false);
        article.setId("1302.2146");
        article.setSubmitter("Michael Saelim");
        article.setTitle("The Same-Sign Dilepton Signature of RPV/MFV SUSY");
        article.setAuthors("Joshua Berger, Maxim Perelstein, Michael Saelim and Philip Tanedo");
        article.setCategories("hep-ph, hep-ex");
        article.setVersions(Sets.newHashSet(v1, v2));

        return article;
    }

}