package com.hanrideb.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.hanrideb.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.hanrideb.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.hanrideb.domain.User.class.getName());
            createCache(cm, com.hanrideb.domain.Authority.class.getName());
            createCache(cm, com.hanrideb.domain.User.class.getName() + ".authorities");
            createCache(cm, com.hanrideb.domain.Ogrenci.class.getName());
            createCache(cm, com.hanrideb.domain.Ogrenci.class.getName() + ".kayitlar2s");
            createCache(cm, com.hanrideb.domain.Ogrenci.class.getName() + ".rozetlers");
            createCache(cm, com.hanrideb.domain.Ogretmen.class.getName());
            createCache(cm, com.hanrideb.domain.Ogretmen.class.getName() + ".derslers");
            createCache(cm, com.hanrideb.domain.Rozet.class.getName());
            createCache(cm, com.hanrideb.domain.Rozet.class.getName() + ".ogrencilers");
            createCache(cm, com.hanrideb.domain.Kayit.class.getName());
            createCache(cm, com.hanrideb.domain.Kayit.class.getName() + ".dersAnalizleris");
            createCache(cm, com.hanrideb.domain.DersAnaliz.class.getName());
            createCache(cm, com.hanrideb.domain.DersAnaliz.class.getName() + ".kayitlar3s");
            createCache(cm, com.hanrideb.domain.Ders.class.getName());
            createCache(cm, com.hanrideb.domain.Ders.class.getName() + ".kayitlar1s");
            createCache(cm, com.hanrideb.domain.Mufredat.class.getName());
            createCache(cm, com.hanrideb.domain.Mufredat.class.getName() + ".bolumlers");
            createCache(cm, com.hanrideb.domain.Bolum.class.getName());
            createCache(cm, com.hanrideb.domain.Bolum.class.getName() + ".analizBolums");
            createCache(cm, com.hanrideb.domain.Bolum.class.getName() + ".testlers");
            createCache(cm, com.hanrideb.domain.Bolum.class.getName() + ".mufredatlars");
            createCache(cm, com.hanrideb.domain.SoruTest.class.getName());
            createCache(cm, com.hanrideb.domain.SoruTest.class.getName() + ".sorulars");
            createCache(cm, com.hanrideb.domain.Soru.class.getName());
            createCache(cm, com.hanrideb.domain.Soru.class.getName() + ".kazanimlars");
            createCache(cm, com.hanrideb.domain.Soru.class.getName() + ".aitOldTestlers");
            createCache(cm, com.hanrideb.domain.SoruKazanimlari.class.getName());
            createCache(cm, com.hanrideb.domain.SoruKazanimlari.class.getName() + ".aitOldSorulars");
            createCache(cm, com.hanrideb.domain.Form.class.getName());
            createCache(cm, com.hanrideb.domain.Form.class.getName() + ".yorumlar1s");
            createCache(cm, com.hanrideb.domain.Yorum.class.getName());
            createCache(cm, com.hanrideb.domain.Blog.class.getName());
            createCache(cm, com.hanrideb.domain.Entry.class.getName());
            createCache(cm, com.hanrideb.domain.Entry.class.getName() + ".tags");
            createCache(cm, com.hanrideb.domain.Tag.class.getName());
            createCache(cm, com.hanrideb.domain.Tag.class.getName() + ".entries");
            createCache(cm, com.hanrideb.domain.SiteInfo.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
