package learning.spring.binarytea.service;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.MenuItemEntity;
import learning.spring.binarytea.model.Size;
import learning.spring.binarytea.repository.MenuJpaRepository;
import learning.spring.binarytea.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@CacheConfig(cacheNames = "menu")
public class MenuService {
    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @Cacheable
    public List<MenuItemEntity> getAllMenu() {
        return menuJpaRepository.findAll();
    }

    @Cacheable(key = "#root.methodName + '-' + #name + '-' + #size")
    public Optional<MenuItemEntity> getByNameAndSize(String name, Size size) {
        return menuJpaRepository.findByNameAndSize(name, size);
    }

    public Optional<MenuItemEntity> getById(Long id) {
        return menuJpaRepository.findById(id);
    }

    public List<MenuItemEntity> getByName(String name) {
        return menuJpaRepository.findAll(Example.of(MenuItemEntity.builder().name(name).build()), Sort.by("id"));
    }

    public List<MenuItemEntity> save(List<MenuItemEntity> items){
        return menuJpaRepository.saveAll(items);
    }

    public MenuItemEntity save(MenuItemEntity menuItem){
        return menuJpaRepository.save(menuItem);
    }
}
