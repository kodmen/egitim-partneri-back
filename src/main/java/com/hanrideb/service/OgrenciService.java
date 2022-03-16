package com.hanrideb.service;

import static com.hanrideb.security.SecurityUtils.getCurrentUserLogin;

import com.hanrideb.domain.Ogrenci;
import com.hanrideb.domain.User;
import com.hanrideb.repository.OgrenciRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OgrenciService {

    private final OgrenciRepository ogrenciRepository;
    private final UserService userService;

    public OgrenciService(OgrenciRepository ogrenciRepository, UserService userService) {
        this.ogrenciRepository = ogrenciRepository;
        this.userService = userService;
    }

    public Ogrenci getByUserId() throws Exception {
        Optional<String> userLogin = getCurrentUserLogin();
        if (userLogin.isPresent()) {
            Optional<User> user = userService.getUserWithAuthoritiesByLogin(userLogin.get());
            if (user.isPresent()) {
                Optional<Ogrenci> ogrenci = ogrenciRepository.findByStudentUser_Id(user.get().getId());

                if (ogrenci.isPresent()) {
                    return ogrenci.get();
                } else {
                    throw new Exception("user id ile öğrenci bulunamadı");
                }
            } else {
                throw new Exception("login bilgisi ile user gelmedi");
            }
        } else {
            throw new Exception("aktif user bulunamadı");
        }
    }
}
