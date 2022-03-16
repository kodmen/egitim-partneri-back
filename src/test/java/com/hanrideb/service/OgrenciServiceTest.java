package com.hanrideb.service;

import static com.hanrideb.security.SecurityUtils.getCurrentUserLogin;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.hanrideb.domain.Ogrenci;
import com.hanrideb.domain.User;
import com.hanrideb.repository.OgrenciRepository;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.AntPathMatcher;

@ExtendWith(MockitoExtension.class)
class OgrenciServiceTest {

    @InjectMocks
    private OgrenciService ogrenciService;

    @Mock
    private OgrenciRepository ogrenciRepository;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("ogrenci getirme id ile çalışan ")
    void getByUserId() throws Exception {
        //        Optional<Ogrenci> ogrenci = Optional.of(Ogrenci.bosOgrenciOlustur());
        //
        //        when(getCurrentUserLogin()).thenReturn(Optional.of("admin"));
        //        when(userService.getUserWithAuthoritiesByLogin(ArgumentMatchers.any())).thenReturn(Optional.of(new User()));
        //        when(ogrenciRepository.findByStudentUser_Id(ArgumentMatchers.any())).thenReturn(ogrenci);
        //
        //        Assert.assertEquals(ogrenci.get(),ogrenciService.getByUserId());
        //        //test et
    }
}
