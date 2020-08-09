package meet_eat.app.repository;

import org.junit.Test;

import java.io.Serializable;

import meet_eat.data.entity.Entity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public abstract class EntityRepositoryTest<T extends EntityRepository<S>, S extends Entity<U>, U extends Serializable>
        extends RepositoryTestEnvironment{

    private T entityRepository;
    private S entityWithId;
    private S entityWithoutId;

    protected EntityRepositoryTest(T entityRepository, S entityWithId, S entityWithoutId) {
        this.entityRepository = entityRepository;
        this.entityWithId = entityWithId;
        this.entityWithoutId = entityWithoutId;
    }

    protected T getEntityRepository() {
        return entityRepository;
    }

    protected S getEntityWithId() {
        return entityWithId;
    }

    protected S getEntityWithoutId() {
        return entityWithoutId;
    }

    // Test addEntity

    @Test(expected = IllegalStateException.class)
    public void testAddEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        entityRepository.addEntity(entityWithoutId);
    }

    @Test(expected = NullPointerException.class)
    public void testAddEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        entityRepository.addEntity(null);
    }

    // Test updateEntity

    @Test(expected = IllegalStateException.class)
    public void testUpdateEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        entityRepository.updateEntity(entityWithId);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        entityRepository.updateEntity(null);
    }

    // Test deleteEntity

    @Test(expected = IllegalStateException.class)
    public void testDeleteEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        entityRepository.deleteEntity(entityWithoutId);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        entityRepository.deleteEntity(null);
    }

    // Test getEntityById

    @Test(expected = IllegalStateException.class)
    public void testGetEntityByIdNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        entityRepository.getEntityById("identifier");
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityByIdWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        entityRepository.getEntityById(null);
    }
}
