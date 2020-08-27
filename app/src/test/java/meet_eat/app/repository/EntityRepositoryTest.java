package meet_eat.app.repository;

import org.junit.Test;

import java.io.Serializable;

import meet_eat.data.entity.Entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public abstract class EntityRepositoryTest<T extends EntityRepository<S>, S extends Entity<U>, U extends Serializable>
        extends RepositoryTestEnvironment{

    private final T entityRepository;
    private final S persistentEntity;
    private final S newEntity;

    protected EntityRepositoryTest(T entityRepository, S persistentEntity, S newEntity) {
        this.entityRepository = entityRepository;
        this.persistentEntity = persistentEntity;
        this.newEntity = newEntity;
    }

    protected T getEntityRepository() {
        return entityRepository;
    }

    protected S getPersistentEntity() {
        return persistentEntity;
    }

    protected S getNewEntity() {
        return newEntity;
    }

    // Test addEntity

    @Test(expected = IllegalStateException.class)
    public void testAddEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        entityRepository.addEntity(newEntity);
    }

    @Test(expected = NullPointerException.class)
    public void testAddEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        entityRepository.addEntity(null);
    }

    @Test
    public void testAddEntityValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        assertNull(newEntity.getIdentifier());
        S fetchedEntity = entityRepository.addEntity(newEntity);

        // Assertions
        assertNotNull(fetchedEntity.getIdentifier());

        entityRepository.deleteEntity(fetchedEntity);
    }

    // Test updateEntity

    @Test(expected = IllegalStateException.class)
    public void testUpdateEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        entityRepository.updateEntity(persistentEntity);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        entityRepository.updateEntity(null);
    }

    @Test
    public void testUpdateEntityValidWithoutChanges() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        S fetchedEntity = entityRepository.updateEntity(persistentEntity);

        // Assertions
        assertNotNull(fetchedEntity.getIdentifier());
        assertEquals(fetchedEntity, persistentEntity);
    }

    // Test deleteEntity

    @Test(expected = IllegalStateException.class)
    public void testDeleteEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        entityRepository.deleteEntity(persistentEntity);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        entityRepository.deleteEntity(null);
    }

    @Test
    public void testDeleteEntityValid() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        S fetchedEntity = entityRepository.addEntity(newEntity);
        assertNotNull(fetchedEntity.getIdentifier());

        // Execution
        entityRepository.deleteEntity(fetchedEntity);
    }

    // Test getEntityById

    @Test(expected = IllegalStateException.class)
    public void testGetEntityByIdNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        entityRepository.getEntityById(persistentEntity.getIdentifier().toString());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityByIdWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        entityRepository.getEntityById(null);
    }

    @Test
    public void testGetEntityByIdValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        assertNotNull(persistentEntity.getIdentifier());
        S fetchedEntity = entityRepository.getEntityById(persistentEntity.getIdentifier().toString());

        // Assertions
        assertEquals(persistentEntity.getIdentifier(), fetchedEntity.getIdentifier());
    }
}
