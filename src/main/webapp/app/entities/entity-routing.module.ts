import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ogrenci',
        data: { pageTitle: 'gamificationApp.ogrenci.home.title' },
        loadChildren: () => import('./ogrenci/ogrenci.module').then(m => m.OgrenciModule),
      },
      {
        path: 'ogretmen',
        data: { pageTitle: 'gamificationApp.ogretmen.home.title' },
        loadChildren: () => import('./ogretmen/ogretmen.module').then(m => m.OgretmenModule),
      },
      {
        path: 'rozet',
        data: { pageTitle: 'gamificationApp.rozet.home.title' },
        loadChildren: () => import('./rozet/rozet.module').then(m => m.RozetModule),
      },
      {
        path: 'kayit',
        data: { pageTitle: 'gamificationApp.kayit.home.title' },
        loadChildren: () => import('./kayit/kayit.module').then(m => m.KayitModule),
      },
      {
        path: 'ders-analiz',
        data: { pageTitle: 'gamificationApp.dersAnaliz.home.title' },
        loadChildren: () => import('./ders-analiz/ders-analiz.module').then(m => m.DersAnalizModule),
      },
      {
        path: 'ders',
        data: { pageTitle: 'gamificationApp.ders.home.title' },
        loadChildren: () => import('./ders/ders.module').then(m => m.DersModule),
      },
      {
        path: 'mufredat',
        data: { pageTitle: 'gamificationApp.mufredat.home.title' },
        loadChildren: () => import('./mufredat/mufredat.module').then(m => m.MufredatModule),
      },
      {
        path: 'bolum',
        data: { pageTitle: 'gamificationApp.bolum.home.title' },
        loadChildren: () => import('./bolum/bolum.module').then(m => m.BolumModule),
      },
      {
        path: 'soru-test',
        data: { pageTitle: 'gamificationApp.soruTest.home.title' },
        loadChildren: () => import('./soru-test/soru-test.module').then(m => m.SoruTestModule),
      },
      {
        path: 'soru',
        data: { pageTitle: 'gamificationApp.soru.home.title' },
        loadChildren: () => import('./soru/soru.module').then(m => m.SoruModule),
      },
      {
        path: 'soru-kazanimlari',
        data: { pageTitle: 'gamificationApp.soruKazanimlari.home.title' },
        loadChildren: () => import('./soru-kazanimlari/soru-kazanimlari.module').then(m => m.SoruKazanimlariModule),
      },
      {
        path: 'form',
        data: { pageTitle: 'gamificationApp.form.home.title' },
        loadChildren: () => import('./form/form.module').then(m => m.FormModule),
      },
      {
        path: 'yorum',
        data: { pageTitle: 'gamificationApp.yorum.home.title' },
        loadChildren: () => import('./yorum/yorum.module').then(m => m.YorumModule),
      },
      {
        path: 'blog',
        data: { pageTitle: 'gamificationApp.blog.home.title' },
        loadChildren: () => import('./blog/blog.module').then(m => m.BlogModule),
      },
      {
        path: 'entry',
        data: { pageTitle: 'gamificationApp.entry.home.title' },
        loadChildren: () => import('./entry/entry.module').then(m => m.EntryModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'gamificationApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'site-info',
        data: { pageTitle: 'gamificationApp.siteInfo.home.title' },
        loadChildren: () => import('./site-info/site-info.module').then(m => m.SiteInfoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
