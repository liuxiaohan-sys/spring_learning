package learning.spring.binarytea.controller;

import learning.spring.binarytea.controller.request.NewMenuItemForm;
import learning.spring.binarytea.model.MenuItemEntity;
import learning.spring.binarytea.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@ResponseBody
@RequestMapping("/menu")
@Slf4j
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping(params = "!name")
    public List<MenuItemEntity> getAll(){
        return menuService.getAllMenu();
    }
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<MenuItemEntity> getById(@PathVariable Long id) {
        return menuService.getById(id);
    }
    @RequestMapping(params = "name", method = RequestMethod.GET)
    public List<MenuItemEntity> getByName(@RequestParam String name){
        return menuService.getByName(name);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Optional<MenuItemEntity> createByForm(@Valid NewMenuItemForm form, BindingResult result,
                                                 HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("绑定参数错误：{}", result.getAllErrors());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return Optional.empty();
        }
        MenuItemEntity item = MenuItemEntity.builder().name(form.getName())
                .price(form.getPrice())
                .size(form.getSize())
                .build();
        return Optional.ofNullable(menuService.save(item));
    }
}
