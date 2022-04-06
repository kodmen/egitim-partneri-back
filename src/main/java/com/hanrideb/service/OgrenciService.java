package com.hanrideb.service;

import static com.hanrideb.security.SecurityUtils.getCurrentUserLogin;

import com.hanrideb.domain.Ogrenci;
import com.hanrideb.domain.User;
import com.hanrideb.repository.OgrenciRepository;
import com.hanrideb.service.utility.LevelUtility;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OgrenciService {

    private final OgrenciRepository ogrenciRepository;
    private final UserService userService;
    private final LevelUtility levelUtility;

    public OgrenciService(OgrenciRepository ogrenciRepository, UserService userService, LevelUtility levelUtility) {
        this.ogrenciRepository = ogrenciRepository;
        this.userService = userService;
        this.levelUtility = levelUtility;
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

    public void OgrenciPuanArttir(float net) throws Exception {
        // veya her net için 10*
        // 6.3 net yaparsa 63 puan kazanır
        // tamam her net 10 puan
        Ogrenci ogrenci = getByUserId();
        var kazanılanPuan = net * 10;
        var ogrPuan = ogrenci.getToplamPuan() + (int) kazanılanPuan;

        var level = levelUtility.levelGetir(ogrPuan);

        if (!ogrenci.getLevel().equals((long) level)) {
            ogrenci.setLevel((long) level);
        }

        ogrenci.setToplamPuan(ogrPuan);

        ogrenciRepository.save(ogrenci);
    }
}
