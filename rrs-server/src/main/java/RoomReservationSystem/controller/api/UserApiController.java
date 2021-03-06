package RoomReservationSystem.controller.api;

import RoomReservationSystem.dto.UserDTO;
import RoomReservationSystem.model.User;
import RoomReservationSystem.service.UserService;
import RoomReservationSystem.validation.UserValidator;
import static RoomReservationSystem.dto.UserDTO.toUserDTO;
import static RoomReservationSystem.dto.UserDTO.toUserDTOList;
import RoomReservationSystem.exception.AuthorityAlredyExistsException;
import RoomReservationSystem.exception.AuthorityNotExistsException;
import RoomReservationSystem.exception.UserAlredyExistsException;
import RoomReservationSystem.exception.UserNotExistsException;
import static RoomReservationSystem.util.ExceptionUtils.handleException;
import static RoomReservationSystem.util.ValidationUtils.concatErrors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A felhasználókhoz tartozó végpontokat tartalmazó osztály
 *
 * @author Tomecz Patrik
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserApiController {

    @Autowired
    UserValidator userValidator;

    @Autowired
    UserService userService;

    /**
     * A függvény ami visszaadja egy listában az összes adatbázisban található
     * felhasználót
     *
     * @return A felhasználók egy listában
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(toUserDTOList(userService.findAll()));
    }

    /**
     * A függvény ami visszaadja egy listában az összes adatbázisban található
     * felhasználó nevét
     *
     * @return A felhasználónevek egy listában
     */
    @GetMapping("/getNames")
    public ResponseEntity getNames() {
        return ResponseEntity.ok(userService.getNames());
    }

    /**
     * A függvény ami visszaadja az adott azonosítóval rendelkező felhasználót
     *
     * @param id Az azonosító
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity findByName(@PathVariable int id) {
        try {
            return ResponseEntity.ok(toUserDTO(userService.findById(id)));
        } catch (UserNotExistsException ex) {
            return handleException(ex);
        }
    }

    /**
     * A függvény ami visszaadja a felhasználónévhez tartozó felhasználót
     *
     * @param username A felhasználónév
     * @return A megfelelő felhasználó
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/findByUsername")
    public ResponseEntity findByUsername(@RequestParam(value = "username", required = true) String username) {
        try {
            return ResponseEntity.ok(toUserDTO(userService.findByUsername(username)));
        } catch (UsernameNotFoundException | UserNotExistsException | NullPointerException ex) {
            return handleException(ex);
        }
    }

    /**
     * A függvény ami visszaadja egy listában az adott nevű felhasználókat
     *
     * @param name A név
     * @return A megfelelő felhasználók egy listában
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/findByName")
    public ResponseEntity findByName(@RequestParam(value = "name", required = true) String name) {
        return ResponseEntity.ok(toUserDTOList(userService.findByName(name)));
    }

    /**
     * A függvény ami létrehozza a megfelelő felhasználót
     *
     * @param userDTO A felhasználó
     * @param bindingResult A BindingResult objektum
     * @return A megfelelő válasz entitás
     */
    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody UserDTO userDTO, BindingResult bindingResult
    ) {
        userValidator.validate(userDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            try {
                User registered = userService.register(userDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(toUserDTO(registered));
            } catch (UserAlredyExistsException | AuthorityNotExistsException | AuthorityAlredyExistsException | NullPointerException ex) {
                return handleException(ex);
            }

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(concatErrors(bindingResult));
        }
    }

    /**
     * A felhasználók frissítéséért felelős függvény
     *
     * @param id Az azonosító
     * @param userDTO A felhasználó
     * @param bindingResult A BindingResult objektum
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            try {
                User updated = userService.update(
                        id,
                        userDTO
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(toUserDTO(updated));
            } catch (UserNotExistsException ex) {
                return handleException(ex);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(concatErrors(bindingResult));
        }
    }

    /**
     * A függvény ami törli az adott felhasználónévhez tartozó felhasználót
     *
     * @param username A felhasználónév
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteByUsername")
    public ResponseEntity deleteByUsername(@RequestParam(value = "username", required = true) String username) {
        try {
            userService.deleteByUsername(username);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UserNotExistsException | NullPointerException ex) {
            return handleException(ex);
        }
    }

    /**
     * A függvény ami visszaadja, hogy létezik-e az adott azonosítóhoz tartozó
     * entitás
     *
     * @param id Az azonosító
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/existsById")
    public ResponseEntity existsById(@RequestParam(value = "id", required = true) int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.existsById(id));
    }

    /**
     * A függvény ami visszaadja, hogy létezik-e az adott felhasználónév tartozó
     * entitás
     *
     * @param username A felhasználónév
     * @return A megfelelő válasz entitás
     */
    @GetMapping("/existsByUsername")
    public ResponseEntity existsByUsername(@RequestParam(value = "username", required = true) String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.existsByUsername(username));
    }

    /**
     * A függvény ami visszaadja, hogy létezik-e az adott e-mail címhez tartozó
     * entitás
     *
     * @param email Az e-mail cím
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/existsByEmail")
    public ResponseEntity existsByEmail(@RequestParam(value = "email", required = true) String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.existsByEmail(email));
    }

    /**
     * A függvény ami visszaadjaa felhasználónévhez tartozó teljes nevet. A
     * Collections.singletonMap() függvény segítségével "mintha" JSON objektumot
     * adnánk tovább
     *
     * @param username A felhasználónév
     * @return A megfelelő válasz entitás
     */
    @GetMapping("/getName")
    public ResponseEntity getName(@RequestParam(value = "username", required = true) String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getName(username));
        } catch (UserNotExistsException ex) {
            return handleException(ex);
        }
    }
}
