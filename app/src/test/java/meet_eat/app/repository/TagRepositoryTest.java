package meet_eat.app.repository;

import org.junit.Test;

import meet_eat.data.entity.Tag;

import static org.junit.Assert.assertNull;

public class TagRepositoryTest extends EntityRepositoryTest<TagRepository, Tag, String> {

    private static final Tag TAG_WITH_ID = new Tag("a8s76d9a39nhc7qxqm87z87", "aldkfmldföldmflmdkfiuh");
    private static final Tag TAG_WITHOUT_ID = new Tag("daonfjdnafjkndfkjnajkfndajdnkf");

    public TagRepositoryTest() {
        super(new TagRepository(), TAG_WITH_ID, TAG_WITHOUT_ID);
    }

    // Test getTags

    @Test(expected = IllegalStateException.class)
    public void testGetTagsNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getTags();
    }

}
