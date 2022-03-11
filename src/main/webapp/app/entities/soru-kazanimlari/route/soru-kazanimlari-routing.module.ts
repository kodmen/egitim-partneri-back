import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SoruKazanimlariComponent } from '../list/soru-kazanimlari.component';
import { SoruKazanimlariDetailComponent } from '../detail/soru-kazanimlari-detail.component';
import { SoruKazanimlariUpdateComponent } from '../update/soru-kazanimlari-update.component';
import { SoruKazanimlariRoutingResolveService } from './soru-kazanimlari-routing-resolve.service';

const soruKazanimlariRoute: Routes = [
  {
    path: '',
    component: SoruKazanimlariComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoruKazanimlariDetailComponent,
    resolve: {
      soruKazanimlari: SoruKazanimlariRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoruKazanimlariUpdateComponent,
    resolve: {
      soruKazanimlari: SoruKazanimlariRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoruKazanimlariUpdateComponent,
    resolve: {
      soruKazanimlari: SoruKazanimlariRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(soruKazanimlariRoute)],
  exports: [RouterModule],
})
export class SoruKazanimlariRoutingModule {}
