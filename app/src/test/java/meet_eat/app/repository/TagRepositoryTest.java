package meet_eat.app.repository;

import meet_eat.data.entity.Tag;

public class TagRepositoryTest extends EntityRepositoryTest<TagRepository, Tag, String> {

    private static final Tag TAG_WITH_ID = new Tag("a8s76d9a39nhc7qxqm87z87", "aldkfmldföldmflmdkfiuh");
    private static final Tag TAG_WITHOUT_ID = new Tag("daonfjdnafjkndfkjnajkfndajdnkf");

    public TagRepositoryTest() {
        super(new TagRepository(), TAG_WITH_ID, TAG_WITHOUT_ID);
    }

    // Test addEntity

    /*@Ignore
    @Test
    public void testAddEntityValidWithId() throws RequestHandlerException {
        Session.getInstance().login(loginCredential);
        assertNotNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(tagWitID);

        // Assertions
        assertEquals(tagWitID, tagRepository.getEntityById(tagWitID.getIdentifier()));
        tagRepository.deleteEntity(tagWitID);
    }*/

    // Test updateEntity

    // Test deleteEntity

    // Test getEntityById

}
