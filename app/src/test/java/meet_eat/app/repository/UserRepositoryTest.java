package meet_eat.app.repository;

import meet_eat.data.entity.user.User;

public class UserRepositoryTest extends EntityRepositoryTest<UserRepository, User, String> {

    public UserRepositoryTest() {
        super(new UserRepository(), null, null);
    }
}
