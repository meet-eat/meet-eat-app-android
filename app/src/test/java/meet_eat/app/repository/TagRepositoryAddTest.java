package meet_eat.app.repository;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import meet_eat.data.entity.Tag;

@Ignore //TODO waiting for Server
public class TagRepositoryAddTest {

    public static List<Tag> validTags;

    @BeforeClass
    public static void initializeTags() {
        validTags.add(new Tag("test1"));
        validTags.add(new Tag("test2"));
        validTags.add(new Tag("id1", "test3"));
        validTags.add(new Tag("id1", "test4"));
    }

    @Test
    public void testAddWithValidTag() throws RequestHandlerException {
        // Execution
        Tag addedTag = new TagRepository().addEntity(validTags.get(0));

        // Assertions
        assertEquals(validTags.get(0).getName(), addedTag.getName());
        assertNull(validTags.get(0).getIdentifier());
        assertNotNull(addedTag.getIdentifier());
    }

    @Test(expected = NullPointerException.class)
    public void testAddWithNull() throws RequestHandlerException {
        // Execution
        Tag addedTag = new TagRepository().addEntity(null);
    }

    @Test(expected = RequestHandlerException.class)
    public void testAddWithInvalidTag() throws RequestHandlerException {
        // Test data
        Tag invalidTag = new Tag(null);

        // Execution
        Tag addedTag = new TagRepository().addEntity(invalidTag);
    }

    @Test(expected = RequestHandlerException.class)
    public void testAddWithExistingTag() throws RequestHandlerException {
        // Execution
        new TagRepository().addEntity(validTags.get(1));
        new TagRepository().addEntity(validTags.get(1));
    }

    @Test(expected = RequestHandlerException.class)
    public void testAddWithExistingTagIdentifier() throws RequestHandlerException {
        // Assertions
        assertEquals(validTags.get(4).getIdentifier(), validTags.get(5).getIdentifier());
        assertNotEquals(validTags.get(4).getName(), validTags.get(5).getName());

        // Execution
        new TagRepository().addEntity(validTags.get(4));
        new TagRepository().addEntity(validTags.get(5));
    }
}
