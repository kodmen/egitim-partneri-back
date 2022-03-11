import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OgretmenComponent } from '../list/ogretmen.component';
import { OgretmenDetailComponent } from '../detail/ogretmen-detail.component';
import { OgretmenUpdateComponent } from '../update/ogretmen-update.component';
import { OgretmenRoutingResolveService } from './ogretmen-routing-resolve.service';

const ogretmenRoute: Routes = [
  {
    path: '',
    component: OgretmenComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OgretmenDetailComponent,
    resolve: {
      ogretmen: OgretmenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OgretmenUpdateComponent,
    resolve: {
      ogretmen: OgretmenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OgretmenUpdateComponent,
    resolve: {
      ogretmen: OgretmenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ogretmenRoute)],
  exports: [RouterModule],
})
export class OgretmenRoutingModule {}
