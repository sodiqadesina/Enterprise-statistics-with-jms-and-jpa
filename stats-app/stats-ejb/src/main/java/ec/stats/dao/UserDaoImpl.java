package ec.stats.dao;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.stats.model.User;
import java.util.List;

@Stateful
public class UserDaoImpl implements UserDao {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public User getUser(String name, String password) {
        try {
            return em.createQuery(
                "SELECT u FROM User u WHERE u.name = :name AND u.password = :password", User.class)
                .setParameter("name", name)
                .setParameter("password", password)
                .getSingleResult();
        } catch (Exception e) {
            return null;  // Handle exception
        }
    }

    @Override
    public User getUserByName(String name) {
        try {
            return em.createQuery(
                "SELECT u FROM User u WHERE u.name = :name", User.class)
                .setParameter("name", name)
                .getSingleResult();
        } catch (Exception e) {
            return null;  // Handle exception
        }
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
