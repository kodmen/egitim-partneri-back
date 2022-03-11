import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DersAnalizComponent } from '../list/ders-analiz.component';
import { DersAnalizDetailComponent } from '../detail/ders-analiz-detail.component';
import { DersAnalizUpdateComponent } from '../update/ders-analiz-update.component';
import { DersAnalizRoutingResolveService } from './ders-analiz-routing-resolve.service';

const dersAnalizRoute: Routes = [
  {
    path: '',
    component: DersAnalizComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DersAnalizDetailComponent,
    resolve: {
      dersAnaliz: DersAnalizRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DersAnalizUpdateComponent,
    resolve: {
      dersAnaliz: DersAnalizRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DersAnalizUpdateComponent,
    resolve: {
      dersAnaliz: DersAnalizRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dersAnalizRoute)],
  exports: [RouterModule],
})
export class DersAnalizRoutingModule {}
