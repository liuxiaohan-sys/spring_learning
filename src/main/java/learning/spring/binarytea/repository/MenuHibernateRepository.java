package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.MenuItemEntity;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class MenuHibernateRepository extends HibernateDaoSupport {
    public MenuHibernateRepository(SessionFactory sessionFactory){ super.setSessionFactory(sessionFactory);}

    public long countMenuItems() {
        return getSessionFactory().getCurrentSession()
                .createQuery("select count(t) from MenuItemEntity t", Long.class)
                .getSingleResult();
    }

    public List<MenuItemEntity> queryAllItems(){ return getHibernateTemplate().loadAll(MenuItemEntity.class);}
    public MenuItemEntity queryForItem(Long id) {
        return getHibernateTemplate().get(MenuItemEntity.class, id);
    }

    public void insertItem(MenuItemEntity item) {
        getHibernateTemplate().save(item);
    }

    public void updateItem(MenuItemEntity item) {
        getHibernateTemplate().update(item);
    }

    public void deleteItem(Long id) {
        MenuItemEntity item = getHibernateTemplate().get(MenuItemEntity.class, id);
        if (item != null) {
            getHibernateTemplate().delete(item);
        }
    }
}
