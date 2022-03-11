import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OgrenciComponent } from '../list/ogrenci.component';
import { OgrenciDetailComponent } from '../detail/ogrenci-detail.component';
import { OgrenciUpdateComponent } from '../update/ogrenci-update.component';
import { OgrenciRoutingResolveService } from './ogrenci-routing-resolve.service';

const ogrenciRoute: Routes = [
  {
    path: '',
    component: OgrenciComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OgrenciDetailComponent,
    resolve: {
      ogrenci: OgrenciRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OgrenciUpdateComponent,
    resolve: {
      ogrenci: OgrenciRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OgrenciUpdateComponent,
    resolve: {
      ogrenci: OgrenciRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ogrenciRoute)],
  exports: [RouterModule],
})
export class OgrenciRoutingModule {}
